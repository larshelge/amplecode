package org.amplecode.cave.process.queue;

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

/**
 * Simple implementation of the {@link ProcessQueueConstraints} interface. Just
 * properties with getters and setters.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ProcessQueueConstraintsImpl.java 58 2008-02-02 14:38:01Z torgeilo $
 */
public class ProcessQueueConstraintsImpl
    implements ProcessQueueConstraints
{
    private String group;

    private boolean isSerialToGroup;

    private boolean isSerialToAll;

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public final void setGroup( String group )
    {
        this.group = group;
    }

    public final String getGroup()
    {
        return group;
    }

    public final void setSerialToGroup( boolean isSerialToGroup )
    {
        this.isSerialToGroup = isSerialToGroup;
    }

    public final boolean isSerialToGroup()
    {
        return isSerialToGroup;
    }

    public final void setSerialToAll( boolean isSerialToAll )
    {
        this.isSerialToAll = isSerialToAll;
    }

    public final boolean isSerialToAll()
    {
        return isSerialToAll;
    }
}
