package org.amplecode.cave.process.test;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.amplecode.cave.process.ProcessCoordinator;
import org.amplecode.cave.process.ProcessExecutor;
import org.amplecode.cave.process.ProcessExecutor.State;
import org.amplecode.cave.process.state.MessageState;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessTest.java 73 2008-02-04 18:16:30Z torgeilo $
 */
public class ProcessTest
{
    private ProcessCoordinator coordinator;

    @Before
    public void initialize()
        throws Exception
    {
        BeanFactory beanFactory = new XmlBeanFactory( new ClassPathResource( "beans.xml" ) );

        coordinator = (ProcessCoordinator) beanFactory.getBean( ProcessCoordinator.ID );
    }

    @Test
    public void testStateAndInterrupt()
    {
        ProcessExecutor p1 = coordinator.newProcess( "d", "test" );
        ProcessExecutor p2 = coordinator.newProcess( "d", "test" );

        ProcessEndWaitLock lock1 = new ProcessEndWaitLock();
        p1.addProcessListener( lock1 );

        coordinator.requestProcessExecution( p1 );
        coordinator.requestProcessExecution( p2 );

        assertEquals( State.CODEBIT_RUNNING, p1.getState().getCode() );
        assertTrue( p1.getState().isRunning() );
        assertFalse( p1.getState().isEnded() );
        assertFalse( p1.getState().isInterrupted() );
        assertNotNull( p1.getState().getStartTime() );
        assertNull( p1.getState().getEndTime() );
        assertTrue( p1.getState() instanceof MessageState );

        assertEquals( 0, p2.getState().getCode() );
        assertFalse( p2.getState().isRunning() );
        assertFalse( p2.getState().isEnded() );
        assertFalse( p2.getState().isInterrupted() );
        assertNull( p2.getState().getStartTime() );
        assertNull( p2.getState().getEndTime() );
        assertTrue( p2.getState() instanceof MessageState );

        coordinator.cancelOrInterruptProcess( p2.getId() );

        assertEquals( State.CODEBIT_CANCELLED, p2.getState().getCode() );
        assertFalse( p2.getState().isRunning() );
        assertFalse( p2.getState().isEnded() );
        assertFalse( p2.getState().isInterrupted() );
        assertTrue( p2.getState().isCancelled() );
        assertNull( p2.getState().getStartTime() );
        assertNull( p2.getState().getEndTime() );

        coordinator.removeCancelledOrEndedProcess( p2.getId() );

        coordinator.cancelOrInterruptProcess( p1.getId() );

        lock1.waitForProcessEnd( 10000 );

        assertEquals( State.CODEBIT_INTERRUPTED | State.CODEBIT_ENDED, p1.getState().getCode() );
        assertFalse( p1.getState().isRunning() );
        assertTrue( p1.getState().isEnded() );
        assertTrue( p1.getState().isInterrupted() );
        assertNotNull( p1.getState().getStartTime() );
        assertNotNull( p1.getState().getEndTime() );

        coordinator.removeCancelledOrEndedProcess( p1.getId() );
    }

    @Test
    public void testQueue()
    {
        ProcessExecutor p1 = coordinator.newProcess( "c", "owner0" );
        ProcessExecutor p2 = coordinator.newProcess( "a", "owner0" );
        ProcessExecutor p3 = coordinator.newProcess( "b", "owner0" );
        ProcessExecutor p4 = coordinator.newProcess( "a", "owner0" );
        ProcessExecutor p5 = coordinator.newProcess( "c", "owner0" );
        ProcessExecutor p6 = coordinator.newProcess( "a", "owner1" );
        ProcessExecutor p7 = coordinator.newProcess( "a", "owner1" );
        ProcessExecutor p8 = coordinator.newProcess( "b", "owner1" );

        assertEquals( "c", p1.getType() );
        assertEquals( "a", p2.getType() );
        assertEquals( "b", p3.getType() );
        assertEquals( "a", p4.getType() );
        assertEquals( "c", p5.getType() );
        assertEquals( "a", p6.getType() );
        assertEquals( "a", p7.getType() );
        assertEquals( "b", p8.getType() );

        assertEquals( "owner0", p1.getOwner() );
        assertEquals( "owner0", p2.getOwner() );
        assertEquals( "owner0", p3.getOwner() );
        assertEquals( "owner0", p4.getOwner() );
        assertEquals( "owner0", p5.getOwner() );
        assertEquals( "owner1", p6.getOwner() );
        assertEquals( "owner1", p7.getOwner() );
        assertEquals( "owner1", p8.getOwner() );

        ProcessEndWaitLock lock1 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock2 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock3 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock4 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock5 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock6 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock7 = new ProcessEndWaitLock();
        ProcessEndWaitLock lock8 = new ProcessEndWaitLock();

        p1.addProcessListener( lock1 );
        p2.addProcessListener( lock2 );
        p3.addProcessListener( lock3 );
        p4.addProcessListener( lock4 );
        p5.addProcessListener( lock5 );
        p6.addProcessListener( lock6 );
        p7.addProcessListener( lock7 );
        p8.addProcessListener( lock8 );

        coordinator.requestProcessExecution( p1 );
        coordinator.requestProcessExecution( p2 );
        coordinator.requestProcessExecution( p3 );
        coordinator.requestProcessExecution( p4 );
        coordinator.requestProcessExecution( p5 );
        coordinator.requestProcessExecution( p6 );
        coordinator.requestProcessExecution( p7 );
        coordinator.requestProcessExecution( p8 );

        try
        {
            coordinator.requestProcessExecution( p1 );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        lock1.waitForProcessEnd( 100 );
        lock2.waitForProcessEnd( 100 );
        lock3.waitForProcessEnd( 100 );
        lock4.waitForProcessEnd( 100 );
        lock5.waitForProcessEnd( 100 );
        lock6.waitForProcessEnd( 100 );
        lock7.waitForProcessEnd( 100 );
        lock8.waitForProcessEnd( 100 );

        assertTrue( p1.getState().isEnded() );
        assertTrue( p2.getState().isEnded() );
        assertTrue( p3.getState().isEnded() );
        assertTrue( p4.getState().isEnded() );
        assertTrue( p5.getState().isEnded() );
        assertTrue( p6.getState().isEnded() );
        assertTrue( p7.getState().isEnded() );
        assertTrue( p8.getState().isEnded() );

        assertTrue( startTime( p1 ) < endTime( p1 ) );
        assertTrue( startTime( p2 ) < endTime( p2 ) );
        assertTrue( startTime( p3 ) < endTime( p3 ) );
        assertTrue( startTime( p4 ) < endTime( p4 ) );
        assertTrue( startTime( p5 ) < endTime( p5 ) );
        assertTrue( startTime( p6 ) < endTime( p6 ) );
        assertTrue( startTime( p7 ) < endTime( p7 ) );
        assertTrue( startTime( p8 ) < endTime( p8 ) );

        assertTrue( endTime( p1 ) <= startTime( p2 ) );
        assertTrue( endTime( p2 ) <= startTime( p3 ) );
        assertTrue( endTime( p3 ) <= startTime( p4 ) );
        assertTrue( endTime( p4 ) <= startTime( p5 ) );

        assertTrue( endTime( p5 ) <= startTime( p6 ) );
        assertTrue( endTime( p5 ) <= startTime( p7 ) );

        assertTrue( endTime( p6 ) <= startTime( p8 ) );
        assertTrue( endTime( p7 ) <= startTime( p8 ) );

        assertEquals( 4, coordinator.getProcessesByType( "a" ).size() );
        assertEquals( 2, coordinator.getProcessesByType( "b" ).size() );
        assertEquals( 2, coordinator.getProcessesByType( "c" ).size() );
        assertEquals( 5, coordinator.getProcessesByOwner( "owner0" ).size() );
        assertEquals( 3, coordinator.getProcessesByOwner( "owner1" ).size() );
    }

    private static final long startTime( ProcessExecutor p )
    {
        return p.getState().getStartTime().getTime();
    }

    private static final long endTime( ProcessExecutor p )
    {
        return p.getState().getEndTime().getTime();
    }
}
