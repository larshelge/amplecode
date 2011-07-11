package org.amplecode.cave.process;

/*
 * Copyright (c) 2008, the original author or authors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the AmpleCode project nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.amplecode.cave.process.queue.ProcessQueueConstraints;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Process executor. Wraps a process which is to be executed.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessExecutor.java 73 2008-02-04 18:16:30Z torgeilo $
 */
public class ProcessExecutor
    implements Runnable, ProcessQueueConstraints
{
    private final Log log = LogFactory.getLog( ProcessExecutor.class );

    private Set<ProcessListener> listeners = new HashSet<ProcessListener>( 2 );

    private Thread thread;

    private String id;

    private String owner;

    private String type;

    private Process<State> process;

    private State state;

    private boolean serialToAll;

    private boolean serialToGroup;

    private String group;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    ProcessExecutor( String id, String owner, String type, Process<State> process )
    {
        this.id = id;
        this.owner = owner;
        this.type = type;
        this.process = process;

        // ---------------------------------------------------------------------
        // Detect state class
        // ---------------------------------------------------------------------

        state = newInstance( detectStateClass( process ) );

        log.debug( "State class: " + state.getClass().getName() + ", PID: " + id );

        // ---------------------------------------------------------------------
        // Detect queue constraints
        // ---------------------------------------------------------------------

        if ( process instanceof ProcessQueueConstraints )
        {
            ProcessQueueConstraints pqc = (ProcessQueueConstraints) process;

            serialToAll = pqc.isSerialToAll();
            serialToGroup = pqc.isSerialToGroup();
            group = pqc.getGroup();
        }
        else
        {
            serialToAll = process instanceof SerialToAll;
            serialToGroup = process instanceof SerialToGroup;

            if ( serialToGroup )
            {
                group = ((SerialToGroup) process).getGroup();
            }
        }
    }

    // -------------------------------------------------------------------------
    // Run
    // -------------------------------------------------------------------------

    /**
     * INTERNAL USE ONLY!
     */
    public final void run()
    {
        try
        {
            process.execute( state );
        }
        catch ( InterruptedException e )
        {
            log.warn( "The process was interrupted", e );

            state.code |= State.CODEBIT_INTERRUPTED;
        }
        catch ( Exception e )
        {
            log.error( "The process threw exception", e );

            state.code |= State.CODEBIT_ERROR;
        }

        state.endTime = System.currentTimeMillis();
        state.code |= State.CODEBIT_ENDED;
        state.code &= ~State.CODEBIT_RUNNING;

        log.debug( "The process ended" );

        fireProcessEnded();
    }

    // -------------------------------------------------------------------------
    // Control methods
    // -------------------------------------------------------------------------

    /**
     * Starts the process. INTERNAL USE ONLY!
     */
    final void start()
    {
        if ( state.code != 0 )
        {
            throw new RuntimeException( "Cannot start this process. Current state is: " + state.code );
        }

        thread = new Thread( this );
        thread.start();

        log.debug( "Starting process" );

        state.startTime = System.currentTimeMillis();
        state.code |= State.CODEBIT_RUNNING;
    }

    /**
     * Cancels or interrupts the thread. Can be called at any time as long as
     * state is non-null. INTERNAL USE ONLY!
     */
    final void cancelOrInterrupt()
    {
        if ( state.code == 0 )
        {
            state.code |= State.CODEBIT_CANCELLED;
        }
        else if ( state.isRunning() )
        {
            state.code |= State.CODEBIT_INTERRUPTED;

            thread.interrupt();
        }
    }

    // -------------------------------------------------------------------------
    // Private methods
    // -------------------------------------------------------------------------

    private static final Class<? extends State> detectStateClass( Process process )
    {
        Type[] types = process.getClass().getGenericInterfaces();

        for ( Type type : types )
        {
            if ( type instanceof ParameterizedType )
            {
                ParameterizedType pType = (ParameterizedType) type;

                if ( pType.getRawType().equals( Process.class ) )
                {
                    if ( pType.getActualTypeArguments().length > 0 )
                    {
                        return (Class<State>) pType.getActualTypeArguments()[0];
                    }

                    break;
                }
            }
        }

        return State.class;
    }

    private static final <T> T newInstance( Class<T> clazz )
    {
        try
        {
            Constructor<T> constructor = clazz.getConstructor( new Class[] {} );
            return constructor.newInstance( new Object[] {} );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Failed to instantiate " + clazz.getName(), e );
        }
    }

    // -------------------------------------------------------------------------
    // HashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public final boolean equals( Object o )
    {
        return this == o;
    }

    @Override
    public final int hashCode()
    {
        if ( id == null )
        {
            return 0;
        }

        return id.hashCode();
    }

    // -------------------------------------------------------------------------
    // Listener methods
    // -------------------------------------------------------------------------

    public final void addProcessListener( ProcessListener listener )
    {
        listeners.add( listener );
    }

    public final void removeProcessListener( ProcessListener listener )
    {
        listeners.remove( listener );
    }

    private final void fireProcessEnded()
    {
        /*
         * Make a copy so that listeners can remove themselves from the set
         * while looping them.
         */
        final Set<ProcessListener> copy = new HashSet<ProcessListener>( listeners );

        final ProcessEvent event = new ProcessEvent( this );

        for ( ProcessListener listener : copy )
        {
            listener.processEnded( event );
        }
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public final String getId()
    {
        return id;
    }

    public final String getOwner()
    {
        return owner;
    }

    public final String getType()
    {
        return type;
    }

    public final Process getProcess()
    {
        return process;
    }

    public final State getState()
    {
        return state;
    }

    public boolean isSerialToGroup()
    {
        return serialToGroup;
    }

    public boolean isSerialToAll()
    {
        return serialToAll;
    }

    public String getGroup()
    {
        return group;
    }

    // -------------------------------------------------------------------------
    // State class
    // -------------------------------------------------------------------------

    /**
     * Default state class. Can be extended if more state information is
     * desired.
     * 
     * @author Torgeir Lorange Ostby
     * @version $Id: ProcessExecutor.java 73 2008-02-04 18:16:30Z torgeilo $
     */
    public static class State
    {
        public static final int CODEBIT_RUNNING = 1;

        public static final int CODEBIT_ENDED = 2;

        public static final int CODEBIT_INTERRUPTED = 4;

        public static final int CODEBIT_ERROR = 8;

        public static final int CODEBIT_CANCELLED = 16;

        // ---------------------------------------------------------------------
        // State
        // ---------------------------------------------------------------------

        private int code;

        private Long startTime;

        private Long endTime;

        // ---------------------------------------------------------------------
        // Getters
        // ---------------------------------------------------------------------

        /**
         * Returns the status code. If code == 0, then the process is queued for
         * execution.
         */
        public final int getCode()
        {
            return code;
        }

        public final boolean isRunning()
        {
            return (code & CODEBIT_RUNNING) != 0;
        }

        /**
         * Only running processes can end.
         */
        public final boolean isEnded()
        {
            return (code & CODEBIT_ENDED) != 0;
        }

        /**
         * Only running processes can be interrupted. An interrupted process can
         * be running or ended.
         */
        public final boolean isInterrupted()
        {
            return (code & CODEBIT_INTERRUPTED) != 0;
        }

        /**
         * Returns true if the process threw an exception.
         */
        public final boolean hasErrored()
        {
            return (code & CODEBIT_ERROR) != 0;
        }

        /**
         * Returns true if the process was cancelled. A cancelled process is a
         * process which was removed from the process queue before being
         * executed.
         */
        public final boolean isCancelled()
        {
            return (code & CODEBIT_CANCELLED) != 0;
        }

        /**
         * Returns the start time, or null if the process has not started.
         */
        public final Date getStartTime()
        {
            if ( startTime == null )
            {
                return null;
            }

            return new Date( startTime );
        }

        /**
         * Returns the end time, or null if the process has not ended.
         */
        public final Date getEndTime()
        {
            if ( endTime == null )
            {
                return null;
            }

            return new Date( endTime );
        }

        /**
         * Returns the amount of milliseconds the process has been running. If
         * the process is not started zero is returned.
         */
        public final long getRunningTimeMillis()
        {
            if ( startTime == null )
            {
                return 0;
            }

            if ( endTime == null )
            {
                return System.currentTimeMillis() - startTime;
            }

            return endTime - startTime;
        }
    }
}
