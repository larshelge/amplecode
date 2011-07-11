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

import static org.junit.Assert.fail;

import java.lang.Thread.State;

import org.junit.Test;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessEndWaitLockTest.java 58 2008-02-02 14:38:01Z torgeilo $
 */
public class ProcessEndWaitLockTest
{
    @Test
    public void testProcessEndWaitLock()
    {
        // Current thread is control thread
        final Thread controlThread = Thread.currentThread();

        // The test is executed in another thread
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                // Run the timeout test
                (new ProcessEndWaitLock()).waitForProcessEnd( 10 );

                // Wake up control thread from sleep
                controlThread.interrupt();
            }
        };

        // Start the test thread
        Thread testThread = new Thread( runnable );
        testThread.start();

        // Wait for the test thread to finish or time out
        try
        {
            Thread.sleep( 1000 );
        }
        catch ( InterruptedException e )
        {
            // Test thread has finished
        }

        // If the test thread has not finished, then stop it and fail
        if ( !testThread.getState().equals( State.TERMINATED ) )
        {
            testThread.interrupt();
            fail();
        }
    }
}
