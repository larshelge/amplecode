package org.amplecode.quick.statementbuilder;

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
 * @author Lars Helge Overland
 */
public class MySqlStatementBuilder
    extends AbstractStatementBuilder
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public MySqlStatementBuilder()
    {
        super();
    }

    // -------------------------------------------------------------------------
    // AbstractStatementBuilder implementation
    // -------------------------------------------------------------------------
 
    public String getInsertStatementOpening()
    {
        final StringBuffer buffer = new StringBuffer();
        
        buffer.append( "INSERT INTO " + tableName + " (" );
        
        for ( String column : columns )
        {
            buffer.append( column + SEPARATOR );
        }
        
        if ( columns.size() > 0 )
        {
            buffer.deleteCharAt( buffer.length() - 1 );
        }
        
        buffer.append( BRACKET_END + " VALUES " );
        
        return buffer.toString();
    }
        
    public String getInsertStatementValues()
    {
        final StringBuffer buffer = new StringBuffer();
        
        buffer.append( BRACKET_START );
        
        for ( String value : values )
        {
            buffer.append( value + SEPARATOR );
        }
        
        if ( values.size() > 0 )
        {
            buffer.deleteCharAt( buffer.length() - 1 );
        }
        
        buffer.append( BRACKET_END + SEPARATOR );
        
        values.clear();
        
        return buffer.toString();
    }
    
    public String getDoubleColumnType()
    {
        return "DOUBLE";
    }
}
