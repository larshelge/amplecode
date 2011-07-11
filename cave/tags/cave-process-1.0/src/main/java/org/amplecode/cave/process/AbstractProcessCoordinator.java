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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.amplecode.cave.process.ProcessExecutor.State;
import org.amplecode.cave.process.queue.ListProcessQueue;
import org.amplecode.cave.process.queue.ProcessQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract implementation of the {@link ProcessCoordinator} interface which
 * implements all methods except the {@link #newProcess(String,String)} method.
 * It is thread-safe. Processes which are requested for execution are added to a
 * queue, and processes in the head of the queue are executed in parallel, but
 * not more than the specified maximum of running processes.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: AbstractProcessCoordinator.java 73 2008-02-04 18:16:30Z torgeilo $
 */
public abstract class AbstractProcessCoordinator
    implements ProcessCoordinator, ProcessListener
{
    private final Log log = LogFactory.getLog( AbstractProcessCoordinator.class );

    /**
     * Value: {@value}.
     */
    public static final int DEFAULT_MAX_RUNNING_PROCESSES = 10;

    private Map<String, ProcessExecutor> processIndex = new HashMap<String, ProcessExecutor>();

    private ProcessQueue<ProcessExecutor> processQueue = new ListProcessQueue<ProcessExecutor>();

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private int maxRunningProcesses = DEFAULT_MAX_RUNNING_PROCESSES;

    /**
     * Sets the maximum number of running processes.
     */
    public final void setMaxRunningProcesses( int maxRunningProcesses )
    {
        this.maxRunningProcesses = maxRunningProcesses;
    }

    // -------------------------------------------------------------------------
    // ProcessCoordinator
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#requestProcessExecution(org.amplecode.cave.process.ProcessExecutor)
     */
    public final synchronized void requestProcessExecution( ProcessExecutor process )
    {
        if ( processIndex.containsKey( process.getId() ) || process.getState().getCode() != 0 )
        {
            throw new RuntimeException( "Cannot request a process for execution twice" );
        }

        process.addProcessListener( this );

        processIndex.put( process.getId(), process );
        processQueue.add( process );

        executeHead();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#cancelOrInterruptProcess(java.lang.String)
     */
    public final synchronized void cancelOrInterruptProcess( String id )
    {
        ProcessExecutor process = getProcess( id );

        if ( process != null && !process.getState().isCancelled() )
        {
            process.cancelOrInterrupt();

            if ( process.getState().isCancelled() )
            {
                processQueue.remove( process );

                log.debug( "Process removed from queue unstarted" );

                executeHead();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#removeEndedProcess(java.lang.String)
     */
    public final void removeCancelledOrEndedProcess( String id )
    {
        ProcessExecutor process = getProcess( id );

        if ( process != null )
        {
            State state = process.getState();

            if ( state.isEnded() || state.isCancelled() )
            {
                processIndex.remove( id );
            }
            else
            {
                throw new RuntimeException( "Cannot remove a process which has not ended or has not been cancelled" );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#getProcess(java.lang.String)
     */
    public final ProcessExecutor getProcess( String id )
    {
        return processIndex.get( id );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#getProcessesByType(java.lang.String)
     */
    public final Collection<ProcessExecutor> getProcessesByType( String type )
    {
        HashSet<ProcessExecutor> subset = new HashSet<ProcessExecutor>();

        for ( ProcessExecutor process : processIndex.values() )
        {
            if ( process.getType().equals( type ) )
            {
                subset.add( process );
            }
        }

        return subset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#getProcessesByOwner(java.lang.String)
     */
    public final Collection<ProcessExecutor> getProcessesByOwner( String owner )
    {
        HashSet<ProcessExecutor> subset = new HashSet<ProcessExecutor>();

        for ( ProcessExecutor process : processIndex.values() )
        {
            if ( process.getOwner() != null && process.getOwner().equals( owner ) )
            {
                subset.add( process );
            }
        }

        return subset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#getAllProcesses()
     */
    public final Collection<ProcessExecutor> getAllProcesses()
    {
        return processIndex.values();
    }

    // -------------------------------------------------------------------------
    // ProcessListener
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessListener#processFinished(org.amplecode.cave.process.ProcessEvent)
     */
    public final synchronized void processEnded( ProcessEvent e )
    {
        ProcessExecutor process = e.getSource();
        process.removeProcessListener( this );

        processQueue.remove( process );

        executeHead();
    }

    // -------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------

    /**
     * Interrupts remaining processes.
     */
    public final void close()
    {
        for ( ProcessExecutor process : processIndex.values() )
        {
            cancelOrInterruptProcess( process.getId() );
        }
    }

    /**
     * Returns an assumed unique id.
     */
    protected String generateId()
    {
        return UUID.randomUUID().toString();
    }

    private void executeHead()
    {
        int numRunning = 0;
        HashSet<ProcessExecutor> notStarted = new HashSet<ProcessExecutor>();

        for ( ProcessExecutor process : processQueue.getHead() )
        {
            if ( process.getState().getCode() == 0 )
            {
                notStarted.add( process );
            }
            else if ( process.getState().isRunning() )
            {
                ++numRunning;
            }
        }

        if ( numRunning < maxRunningProcesses )
        {
            Iterator<ProcessExecutor> it = notStarted.iterator();

            while ( numRunning < maxRunningProcesses && it.hasNext() )
            {
                it.next().start();
                ++numRunning;
            }
        }
    }
}
