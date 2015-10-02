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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.amplecode.quick.StatementBuilder;

/**
 * @author Lars Helge Overland
 */
public abstract class AbstractStatementBuilder
    implements StatementBuilder
{
    protected final String QUOTE = "'";
    protected final String NULL = "null";
    protected final String TRUE = "true";
    protected final String FALSE = "false";
    protected final String SEPARATOR = ",";
    protected final String BRACKET_START = "(";
    protected final String BRACKET_END = ")";

    protected String tableName = null;
    
    protected String autoIncrementColumn = null;
    
    protected List<String> identifierColums;
    protected List<String> identifierValues;
    
    protected List<String> matchColumns;
    protected List<String> matchValues;
    
    protected List<String> uniqueColumns;
    protected List<String> uniqueValues;
        
    protected List<String> columns;
    protected List<String> values;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public AbstractStatementBuilder()
    {
        identifierColums = new ArrayList<String>();
        identifierValues = new ArrayList<String>();
        
        uniqueColumns = new ArrayList<String>();
        uniqueValues = new ArrayList<String>();
        
        matchColumns = new ArrayList<String>();
        matchValues = new ArrayList<String>();
        
        columns = new ArrayList<String>();
        values = new ArrayList<String>();
    }

    // -------------------------------------------------------------------------
    // StatementBuilder implementation
    // -------------------------------------------------------------------------

    public final void setTableName( String name )
    {
        this.tableName = name;
    }

    public final void setAutoIncrementColumn( String column )
    {
        this.autoIncrementColumn = column;
    }
    
    public final void setIdentifierColumn( String column )
    {
        identifierColums.add( column );
    }
    
    public final void setIdentifierValue( Object value )
    {
        identifierValues.add( defaultEncode( value ) );
    }
    
    public final void setMatchColumn( String column )
    {
        matchColumns.add( column );
    }
    
    public final void setMatchValue( Object value )
    {
        matchValues.add( defaultEncode( value ) );
    }
        
    public final void setUniqueColumn( String column )
    {
        uniqueColumns.add( column );
    }
    
    public final void setUniqueValue( Object value )
    {
        uniqueValues.add( defaultEncode( value ) );
    }
    
    public final void setColumn( String column )
    {
        columns.add( column );
    }
    
    public final void setValue( Object value )
    {
        values.add( defaultEncode( value ) );
    }

    public String getNoColumnInsertStatementOpening()
    {
        return "INSERT INTO " + tableName + " VALUES ";
    }

    public String getUpdateStatement()
    {
        final StringBuffer buffer = new StringBuffer( "UPDATE " + tableName + " SET " );
        
        for ( int i = 0; i < columns.size(); i++ )
        {
            buffer.append( columns.get( i ) + "=" + values.get( i ) );
            
            if ( i + 1 < columns.size() )
            {
                buffer.append( SEPARATOR );
            }
        }
        
        buffer.append( " WHERE " );
        
        for ( int i = 0; i < identifierColums.size(); i++ )
        {
            buffer.append( identifierColums.get( i ) + "=" + identifierValues.get( i ) );
            
            if ( ( i + 1 ) < identifierColums.size() )
            {
                buffer.append( " AND " );
            }
        }
        
        buffer.append( ";" );
        
        values.clear();
        identifierValues.clear();
        
        return buffer.toString();
    }

    public String getDeleteStatement()
    {
        final StringBuffer buffer = new StringBuffer().
            append( "DELETE FROM " ).append( tableName ).append( " WHERE " );
        
        for ( int i = 0; i < identifierColums.size(); i++ )
        {
            buffer.append( identifierColums.get( i ) + "=" + identifierValues.get( i ) );
            
            if ( ( i + 1 ) < identifierColums.size() )
            {
                buffer.append( " AND " );
            }
        }

        buffer.append( ";" );
        
        identifierValues.clear();
        
        return buffer.toString();            
    }
    
    public String getUniquenessStatement( boolean inclusive )
    {
        final String operator = inclusive ? " AND " : " OR ";
                
        final StringBuffer buffer = new StringBuffer().
            append( "SELECT " ).append( uniqueColumns.get( 0 ) ).append( " FROM " ).append( tableName ).append( " WHERE " );
        
        for ( int i = 0; i < uniqueColumns.size(); i++ )
        {
            buffer.append( uniqueColumns.get( i ) + "=" + uniqueValues.get( i ) );
            
            if ( i + 1 < uniqueColumns.size() )
            {
                buffer.append( operator );
            }
        }
        
        uniqueValues.clear();
        
        return buffer.toString();
    }
    
    public String getIdentifierStatement()
    {        
        final StringBuffer buffer = new StringBuffer().
            append( "SELECT " ).append( identifierColums.get( 0 ) ).append( " FROM " ).append( tableName ).append( " WHERE " );
        
        for ( int i = 0; i < matchColumns.size(); i++ )
        {
            buffer.append( matchColumns.get( i ) + "=" + matchValues.get( i ) );
            
            if ( i + 1 < matchColumns.size() )
            {
                buffer.append( " AND " );
            }
        }
        
        matchValues.clear();
        
        return buffer.toString();
    }
    
    public List<String> getUniqueValues()
    {
        List<String> list = new ArrayList<>( uniqueValues );
        uniqueValues.clear();
        return list;
    }
        
    public void setMatchColumnToFirstUniqueColumn()
    {
        if ( uniqueColumns.size() > 0 )
        {
            matchColumns.add( 0, uniqueColumns.get( 0 ) );
        }
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    protected final String defaultEncode( Object value )
    {
        String encoded = NULL;
        
        if ( value != null )
        {
            final Class<?> clazz = value.getClass();
            
            if ( clazz.equals( String.class ) )
            {
                encoded = encodeString( (String) value );
            }
            else if ( clazz.equals( Integer.class ) || clazz.equals( int.class ) )
            {
                encoded = encodeInteger( (Integer) value );
            }
            else if ( clazz.equals( Double.class ) || clazz.equals( double.class ) )
            {
                encoded = encodeDouble( (Double) value );
            }
            else if ( clazz.equals( Boolean.class ) || clazz.equals( boolean.class ) )
            {
                encoded = encodeBoolean( (Boolean) value );
            }
            else if ( clazz.equals( Date.class ) || clazz.equals( java.sql.Date.class ) || 
                clazz.equals( Timestamp.class ) || clazz.equals( Time.class ) )
            {
                encoded = encodeDate( (Date) value );
            }
            else
            {
                encoded = (String) value;
            }
        }
        
        return encoded;
    }

    // -------------------------------------------------------------------------
    // Methods to be overridden by subclasses to change behaviour
    // -------------------------------------------------------------------------

    protected String encodeString( String value )
    {
        if ( value != null )
        {
            value = value.endsWith( "\\" ) ? value.substring( 0, value.length() - 1 ) : value;
            value = value.replaceAll( QUOTE, QUOTE + QUOTE );
        }
        
        return QUOTE + value + QUOTE;
    }
    
    protected String encodeInteger( Integer value )
    {
        return String.valueOf( value );
    }
    
    protected String encodeDouble( Double value )
    {
        return String.valueOf( value );
    }
    
    protected String encodeBoolean( Boolean value )
    {
        return value ? TRUE : FALSE;
    }
    
    protected String encodeDate( Date value )
    {
        Calendar cal = Calendar.getInstance();
        
        cal.setTime( value );
        
        int year = cal.get( Calendar.YEAR );
        int month = cal.get( Calendar.MONTH ) + 1;
        int day = cal.get( Calendar.DAY_OF_MONTH );
        
        String yearString = String.valueOf( year );
        String monthString = month < 10 ? "0" + month : String.valueOf( month );
        String dayString = day < 10 ? "0" + day : String.valueOf( day );
        
        return QUOTE + yearString + "-" + monthString + "-" + dayString + QUOTE;
    }
}
