package org.amplecode.quick.statement;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.amplecode.quick.StatementHolder;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultStatementHolder
    implements StatementHolder
{
    private Connection connection;
    
    private boolean pooled;

    private Statement statement;
    
    public DefaultStatementHolder( Connection connection, boolean pooled )
    {
        this.connection = connection;
        this.pooled = pooled;
        
        try
        {
            this.statement = connection.createStatement();
        }
        catch ( SQLException ex )
        {
            forceClose();
            
            throw new RuntimeException( "Failed to create statement", ex );            
        }
    }
    
    public Statement getStatement()
    {
        return statement;
    }
    
    public Connection getConnection()
    {
        return connection;
    }
    
    public boolean isPooled()
    {
        return pooled;
    }
    
    public int executeUpdate( final String sql )
    {
        return executeUpdate( sql, false );
    }
    
    public int executeUpdate( final String sql, final boolean ignoreException )
    {
        try
        {
            return statement.executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            if ( !ignoreException )
            {
                forceClose();
            }
            
            throw new RuntimeException( ex );
        }
        finally
        {
            close();
        }
    }
    
    public Integer queryForInteger( final String sql )
    {
        try
        {
            final ResultSet resultSet = statement.executeQuery( sql );
            
            return resultSet.next() ? resultSet.getInt( 1 ) : null;
        }
        catch ( SQLException ex )
        {
            forceClose();
            
            throw new RuntimeException( ex );
        }
        finally
        {
            close();
        }
    }

    public Double queryForDouble( final String sql )
    {
        try
        {
            final ResultSet resultSet = statement.executeQuery( sql );
            
            return resultSet.next() ? resultSet.getDouble( 1 ) : null;
        }
        catch ( SQLException ex )
        {
            forceClose();
            
            throw new RuntimeException( ex );
        }
        finally
        {
            close();
        }
    }
    
    public String queryForString( final String sql )
    {
        try
        {
            final ResultSet resultSet = statement.executeQuery( sql );
            
            return resultSet.next() ? resultSet.getString( 1 ) : null;
        }
        catch ( SQLException ex )
        {
            forceClose();
            
            throw new RuntimeException( ex );
        }
        finally
        {
            close();
        }
    }
    
    public int update( final String sql )
    {
        try
        {
            return statement.executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            forceClose();
            
            throw new RuntimeException( ex );
        }
        finally
        {
            close();
        }
    }

    public void close()
    {
        if ( !pooled )
        {
            forceClose();
        }
    }
    
    public void forceClose()
    {
        if ( statement != null )
        {
            try
            {
                statement.close();
            }
            catch ( SQLException ex )
            {
            }
        }
        
        if ( connection != null )
        {
            try
            {
                connection.close();
            }
            catch ( SQLException ex )
            {   
            }
        }
    }
}
