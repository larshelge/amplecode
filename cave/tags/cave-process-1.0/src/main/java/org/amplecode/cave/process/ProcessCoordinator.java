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

/**
 * Coordinates the execution of {@link ProcessExecutor}s.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessCoordinator.java 73 2008-02-04 18:16:30Z torgeilo $
 */
public interface ProcessCoordinator
{
    String ID = ProcessCoordinator.class.getName();

    /**
     * Loads an instance of a new process specified by the given type. The
     * process should then be prepared as necessary by the client and requested
     * for execution.
     * 
     * @param type The process type.
     * @param owner The process owner. Can be null.
     */
    ProcessExecutor newProcess( String type, String owner );

    /**
     * Adds a prepared process to the execution queue and executes it in turn.
     */
    void requestProcessExecution( ProcessExecutor process );

    /**
     * Removes the process from the execution queue if it has not been started
     * yet, or tries to interrupt it if it is running. If a process aborts on an
     * interrupt depends on the process implementation.
     */
    void cancelOrInterruptProcess( String id );

    /**
     * Removes a cancelled or ended process from the coordinator. Should be
     * called by the client when the process has ended or been cancelled.
     */
    void removeCancelledOrEndedProcess( String id );

    /**
     * Returns the process with the given ID, or null if it is not found.
     */
    ProcessExecutor getProcess( String id );

    /**
     * Returns all processes, finished or not, of a certain type.
     */
    Collection<ProcessExecutor> getProcessesByType( String type );

    /**
     * Returns all processes, finished or not, with a certain owner.
     */
    Collection<ProcessExecutor> getProcessesByOwner( String owner );

    /**
     * Returns all processes, finished or not, in no particular order.
     */
    Collection<ProcessExecutor> getAllProcesses();
}
