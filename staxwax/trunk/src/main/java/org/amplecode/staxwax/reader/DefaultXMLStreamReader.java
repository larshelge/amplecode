package org.amplecode.staxwax.reader;

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

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.amplecode.staxwax.XMLException;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultXMLStreamReader.java 152 2009-10-30 23:01:04Z larshelg $
 */
public class DefaultXMLStreamReader
    implements XMLReader
{
    private XMLStreamReader reader;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public DefaultXMLStreamReader( XMLStreamReader reader )
    {
        this.reader = reader;        
    }

    // -------------------------------------------------------------------------
    // XMLReader implementation
    // -------------------------------------------------------------------------
    
    @Override
    public String getElementName()
    {
        final int eventType = reader.getEventType();
        
        return eventType == START_ELEMENT || eventType == END_ELEMENT ? reader.getLocalName() : null;
    }

    @Override
    public String getElementValue()
    {
        try
        {
            reader.next();
            
            return this.getText();
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to get element value", ex );
        }
    }

    @Override
    public void moveToStartElement( String name )
    {
        try
        {
            while( reader.next() != END_DOCUMENT )
            {
                if ( reader.getEventType() == START_ELEMENT && reader.getLocalName().equals( name ) )
                {
                    break;
                }
            }
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to move to start element", ex );
        }
    }

    @Override
    public boolean moveToStartElement( String startElementName, String endElementName )
    {
        try
        {
            while ( reader.next() != END_DOCUMENT ) //TODO && hasNext ?
            {                
                if ( reader.getEventType() == START_ELEMENT && reader.getLocalName().equals( startElementName ) )
                {
                    return true;
                }
                
                if ( reader.getEventType() == END_ELEMENT && reader.getLocalName().equals( endElementName ) )
                {
                    return false;
                }
            }
            
            return false;
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to move to start element", ex );
        }
    }

    @Override
    public boolean isStartElement( String name )
    {
        return reader.getEventType() == START_ELEMENT && reader.getLocalName().equals( name );
    }

    @Override
    public boolean isEndElement( String name )
    {
        return reader.getEventType() == END_ELEMENT && reader.getLocalName().equals( name );
    }

    @Override
    public boolean next()
    {
        try
        {
            return reader.next() != END_DOCUMENT;                
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to move cursor to next element", ex );
        }
    }

    @Override
    public boolean next( String endElementName )
    {
        try
        {
            return !( reader.next() == END_ELEMENT && reader.getLocalName().equals( endElementName ) );                 
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to move cursor to next element", ex );
        }
    }

    @Override
    public String getAttributeValue( String attributeName )
    {
        return nullIfEmpty( reader.getAttributeValue( null, attributeName ) );
    }

    @Override
    public int getAttributeCount()
    {
        return reader.getAttributeCount();
    }

    @Override
    public Map<String, String> readElements( String elementName )
    {
        try
        {
            final Map<String, String> elements = new HashMap<String, String>();
            
            String currentElementName = null;
            
            while ( reader.hasNext() )
            {
                if ( reader.getEventType() == END_ELEMENT && reader.getLocalName().equals( elementName ) )
                {
                    break;
                }
                
                if ( reader.getEventType() == START_ELEMENT )
                {
                    // Read attributes
                    
                    for ( int i = 0; i < reader.getAttributeCount(); i++ )
                    {
                        elements.put( reader.getAttributeLocalName( i ), nullIfEmpty( reader.getAttributeValue( i ) ) );
                    }                    
                    
                    currentElementName = reader.getLocalName();
                    
                    reader.next();
                    
                    // Read text if any
                    
                    elements.put( currentElementName, this.getText() );
                }
                else
                {
                    reader.next();
                }
            }
            
            return elements;
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to read elements", ex );
        }
    }

    @Override
    public XMLStreamReader getXmlStreamReader()
    {
        return reader;
    }

    @Override
    public void closeReader()
    {
        try
        {
            reader.close();
        }
        catch ( XMLStreamException ex )
        {
            throw new XMLException( "Failed to close reader", ex );
        }
    }
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    protected String getText()
        throws XMLStreamException
    {
        StringBuffer sb = new StringBuffer();

        while ( reader.isCharacters() || reader.getEventType() == XMLStreamConstants.CDATA )
        {
            sb.append( reader.getText() );
            reader.next();
        }

        return sb.length() == 0 ? null : sb.toString();
    }

    private String nullIfEmpty( String value )
    {
        return value != null && value.isEmpty() ? null : value;
    }
}
