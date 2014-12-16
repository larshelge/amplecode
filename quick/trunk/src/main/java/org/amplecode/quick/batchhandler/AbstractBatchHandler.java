package org.amplecode.quick.batchhandler;

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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.amplecode.quick.BatchHandler;
import org.amplecode.quick.JdbcConfiguration;
import org.amplecode.quick.StatementBuilder;
import org.amplecode.quick.factory.IdentifierExtractorFactory;
import org.amplecode.quick.factory.StatementBuilderFactory;
import org.amplecode.quick.identifier.IdentifierExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public abstract class AbstractBatchHandler<T>
    implements BatchHandler<T>
{
    private Log log = LogFactory.getLog( AbstractBatchHandler.class );
    
    private JdbcConfiguration configuration;
    
    private Connection connection;
    
    private Statement statement;
    
    protected StatementBuilder statementBuilder;
    
    private IdentifierExtractor identifierExtractor;
    
    private StringBuffer sqlBuffer;
    
    private final Set<String> uniqueValues = new HashSet<>();
    
    private final int maxLength = 200000; // Number of characters in statement accepted by DBMS

    private Collection<Integer> identifiers;

    private int statementCount = 0;
    
    private boolean hasNoAutoIncrementPrimaryKey = false;
    
    private boolean uniqueColumnsAreInclusive = false;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    @SuppressWarnings( "unused" )
    private AbstractBatchHandler()
    {   
    }
    
    protected AbstractBatchHandler( JdbcConfiguration configuration, boolean hasNoAutoIncrementPrimaryKey, boolean uniqueColumnsAreInclusive )
    {
        this.configuration = configuration;
        this.statementBuilder = StatementBuilderFactory.createStatementBuilder( configuration.getDialect() );
        this.identifierExtractor = IdentifierExtractorFactory.createIdentifierExtractor( configuration.getDialect() );
        this.hasNoAutoIncrementPrimaryKey = hasNoAutoIncrementPrimaryKey;
        this.uniqueColumnsAreInclusive = uniqueColumnsAreInclusive;
    }

    // -------------------------------------------------------------------------
    // BatchHandler implementation
    // -------------------------------------------------------------------------
    
    public final BatchHandler<T> init()
    {
        try
        {
            Class.forName( configuration.getDriverClass() );
            
            connection = DriverManager.getConnection( 
                configuration.getConnectionUrl(),
                configuration.getUsername(),
                configuration.getPassword() );

            this.sqlBuffer = new StringBuffer( maxLength );
            this.identifiers = new ArrayList<Integer>();
            this.statementCount = 0;
            
            statement = connection.createStatement();
            
            setTableName();
            setAutoIncrementColumn();
            setIdentifierColumns();
            setUniqueColumns();
            setMatchColumns();
            setColumns();
                        
            this.sqlBuffer.append( getInsertStatementOpening() ); // Initial opening for addObject
            
            return this;
        }
        catch ( Exception ex )
        {
            close();
            
            throw new RuntimeException( "Failed to create statement", ex );
        }
    }
    
    public JdbcConfiguration getConfiguration()
    {
        return configuration;
    }
    
    public BatchHandler<T> setTableName( String name )
    {
        statementBuilder.setTableName( name );
        
        return this;
    }
        
    public final int insertObject( T object, boolean returnGeneratedIdentifier )
    {
        try
        {
            setValues( object );
            
            sqlBuffer = new StringBuffer( getInsertStatementOpening() );
                        
            sqlBuffer.append( statementBuilder.getInsertStatementValues() );
            
            sqlBuffer.deleteCharAt( sqlBuffer.length() - 1 ); 
            
            log.debug( "Insert SQL: " + sqlBuffer );
            
            statement.executeUpdate( sqlBuffer.toString() );
            
            return returnGeneratedIdentifier ? identifierExtractor.extract( statement ) : 0;
        }
        catch ( SQLException ex )
        {
            log.info( "Insert SQL: " + sqlBuffer );

            close();
            
            throw new RuntimeException( "Failed to insert " + object.getClass().getName(), ex );
        }
    }
    
    public final void addObject( T object )
    {
        setUniqueValues( object );
        
        List<String> uniqueList = statementBuilder.getUniqueValues();
        
        String uniqueKey = StringUtils.collectionToCommaDelimitedString( uniqueList );
        
        boolean exists = uniqueList != null && !uniqueList.isEmpty() ? !uniqueValues.add( uniqueKey ) : false;
        
        if ( exists )
        {
            log.warn( "Duplicate object: " + object );
            
            return;
        }

        setValues( object );
        
        sqlBuffer.append( statementBuilder.getInsertStatementValues() );        
        
        statementCount++;
        
        if ( sqlBuffer.length() > maxLength )
        {
            try
            {
                sqlBuffer.deleteCharAt( sqlBuffer.length() - 1 );
                
                statement.executeUpdate( sqlBuffer.toString() );
                
                log.debug( "Add SQL: " + sqlBuffer );
                
                if ( !hasNoAutoIncrementPrimaryKey )
                {
                    identifiers.addAll( identifierExtractor.extract( statement, statementCount ) );
                }
                
                sqlBuffer = new StringBuffer( maxLength ).append( getInsertStatementOpening() );
                
                statementCount = 0;
                
                uniqueValues.clear();
            }
            catch ( SQLException ex )
            {
                log.info( "Add SQL: " + sqlBuffer );

                close();
                
                throw new RuntimeException( "Failed to add objects", ex );
            }
        }
    }
    
    public final void updateObject( T object )
    {        
        setIdentifierValues( object );
        
        setValues( object );
        
        final String sql = statementBuilder.getUpdateStatement();
        
        log.debug( "Update SQL: " + sql );
        
        try
        {
            statement.executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            log.info( "Update SQL: " + sql );

            close();
            
            throw new RuntimeException( "Failed to update object", ex );
        }
    }
    
    public final void deleteObject( T object )
    {
        setIdentifierValues( object );
        
        final String sql = statementBuilder.getDeleteStatement();
        
        log.debug( "Delete SQL: " + sql );
        
        try
        {
            statement.executeUpdate( sql );
        }
        catch ( SQLException ex )
        {
            log.info( "Delete SQL: " + sql );

            close();
            
            throw new RuntimeException( "Failed to delete object", ex );
        }
    }

    public final boolean objectExists( T object )
    {        
        setUniqueValues( object );
        
        final String sql = statementBuilder.getUniquenessStatement( uniqueColumnsAreInclusive );
        
        log.debug( "Unique SQL: " + sql );
        
        try
        {
            return statement.executeQuery( sql ).next();
        }
        catch ( SQLException ex )
        {
            log.info( "Unique SQL: " + sql );

            close();
            
            throw new RuntimeException( "Failed to check uniqueness of object", ex );            
        }
    }
    
    public final int getObjectIdentifier( Object object )
    {        
        setMatchValues( object );
        
        final String sql = statementBuilder.getIdentifierStatement();
        
        log.debug( "Identifier SQL: " + sql );
        
        try
        {
            final ResultSet resultSet = statement.executeQuery( sql );  
            
            return resultSet.next() ? resultSet.getInt( 1 ) : 0;
        }
        catch ( SQLException ex )
        {
            log.info( "Identifier SQL: " + sql );

            close();
            
            throw new RuntimeException( "Failed to get object identifier", ex );            
        }        
    }
    
    public final Collection<Integer> flush()
    {
        try
        {
            if ( sqlBuffer.length() > 2 && statementCount != 0 )
            {
                sqlBuffer.deleteCharAt( sqlBuffer.length() - 1 );
                
                log.debug( "Flush SQL: " + sqlBuffer );
                
                statement.executeUpdate( sqlBuffer.toString() );
                
                if ( !hasNoAutoIncrementPrimaryKey )
                {
                    identifiers.addAll( identifierExtractor.extract( statement, statementCount ) );
                }
                
                statementCount = 0;
                
                uniqueValues.clear();
            }
            
            return identifiers;
        }
        catch ( SQLException ex )
        {
            log.info( "Flush SQL: " + sqlBuffer );
            
            throw new RuntimeException( "Failed to flush BatchHandler", ex );
        }
        finally
        {
            close();
        }
    }

    private void close()
    {
        if ( statement != null )
        {
            try
            {
                statement.close();
            }
            catch ( SQLException statementEx )
            {
                statementEx.printStackTrace();
            }
        }
        
        if ( connection != null )
        {
            try
            {
                connection.close();
            }
            catch ( SQLException connectionEx )
            {
                connectionEx.printStackTrace();
            }
        }
    }
    
    // -------------------------------------------------------------------------
    // Override set-methods
    // -------------------------------------------------------------------------

    /**
     * Should be overridden by subclasses with auto-increment columns.
     */
    protected void setAutoIncrementColumn()
    {   
    }
    
    /**
     * Should be overridden by subclasses with primary key columns.
     */
    protected void setIdentifierColumns()
    {   
    }
    
    /**
     * Should be overridden by subclasses with primary key columns.
     */
    protected void setIdentifierValues( T object )
    {   
    }

    /**
     * Could be overridden by subclasses with unique columns.
     */
    protected void setMatchColumns()
    {
        statementBuilder.setMatchColumnToFirstUniqueColumn();
    }

    /**
     * Could be overridden by subclasses with unique columns.
     */
    protected void setMatchValues( Object object )
    {
        statementBuilder.setMatchValue( object );
    }
    
    // -------------------------------------------------------------------------
    // Override get-methods
    // -------------------------------------------------------------------------

    /**
     * Could be overridden by subclasses in special cases.
     */
    protected String getInsertStatementOpening()
    {
        return statementBuilder.getInsertStatementOpening();
    }
    
    // -------------------------------------------------------------------------
    // Abstract set-methods
    // -------------------------------------------------------------------------

    protected abstract void setTableName();
    
    protected abstract void setUniqueColumns();
    
    protected abstract void setUniqueValues( T object );
    
    protected abstract void setColumns();
    
    protected abstract void setValues( T object );
}
