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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * {@link ProcessCoordinator} which loads process objects from the Spring
 * BeanFactory. The {@link #newProcess(String,String)} type argument is combined
 * with a prefix and a postfix to form a bean ID. According to the default
 * values, a type "a" is transformed into the bean ID "internal-process-a".
 * Exceptions are thrown if there is trouble loading the bean. Process beans
 * must be prototypes.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SpringProcessCoordinator.java 73 2008-02-04 18:16:30Z torgeilo $
 */
public class SpringProcessCoordinator
    extends AbstractProcessCoordinator
    implements BeanFactoryAware
{
    private final Log log = LogFactory.getLog( SpringProcessCoordinator.class );

    /**
     * Value: {@value}.
     */
    public static final String DEFAULT_PROCESS_ID_PREFIX = "internal-process-";

    /**
     * Value: {@value}.
     */
    public static final String DEFAULT_PROCESS_ID_POSTFIX = "";

    private BeanFactory beanFactory;

    private String processIdPrefix = DEFAULT_PROCESS_ID_PREFIX;

    private String processIdPostfix = DEFAULT_PROCESS_ID_POSTFIX;

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public final void setBeanFactory( BeanFactory beanFactory )
    {
        this.beanFactory = beanFactory;
    }

    public final void setProcessIdPrefix( String processIdPrefix )
    {
        this.processIdPrefix = processIdPrefix;
    }

    public final void setProcessIdPostfix( String processIdPostfix )
    {
        this.processIdPostfix = processIdPostfix;
    }

    // -------------------------------------------------------------------------
    // ProcessCoordinator
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.process.ProcessCoordinator#newProcessExecutor(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings( "unchecked" )
    public final ProcessExecutor newProcess( final String type, final String owner )
    {
        if ( type == null )
        {
            throw new IllegalArgumentException( "Type argument is null" );
        }

        final String beanId = processIdPrefix + type + processIdPostfix;

        Object bean = beanFactory.getBean( beanId );

        if ( bean == null )
        {
            throw new IllegalArgumentException( "Process type not found: " + type );
        }

        if ( !Process.class.isAssignableFrom( bean.getClass() ) )
        {
            throw new IllegalArgumentException( "Argument does not reference a process type: " + type );
        }

        if ( !beanFactory.isPrototype( beanId ) )
        {
            throw new IllegalStateException( "Process bean \"" + beanId + "\" is not a prototype" );
        }

        ProcessExecutor executor = new ProcessExecutor( generateId(), owner, type, (Process) bean );

        log.debug( "Created new process executor, type: " + type );

        return executor;
    }
}
