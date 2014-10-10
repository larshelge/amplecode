package org.amplecode.staxwax.factory;

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

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.amplecode.staxwax.reader.DefaultXMLStreamReader;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.DefaultXMLStreamWriter;
import org.amplecode.staxwax.writer.XMLWriter;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;

/**
 * This is a factory class which produces XMLWriter and XMLReader instances.
 * Woodstox is used as implementation classes for the underlying XMLOutputFactory
 * and XMLInputFactory interfaces.
 * 
 * @author Lars Helge Overland
 * @version $Id: XMLFactory.java 151 2009-10-28 15:33:31Z larshelg $
 */
public class XMLFactory
{
    /**
     * Creates an XMLWriter from a StAX-based XMLStreamWriter.
     * 
     * @param outputStream the OutputStream to write to.
     * @return an XMLWriter.
     */
    public static XMLWriter getXMLWriter( OutputStream outputStream )
    {
        try
        {
            XMLOutputFactory factory = new WstxOutputFactory();
            
            XMLStreamWriter streamWriter = factory.createXMLStreamWriter( outputStream );

            return new DefaultXMLStreamWriter( streamWriter );
        }
        catch ( XMLStreamException ex )
        {
            throw new RuntimeException( "Failed to create XMLWriter", ex );
        }
    }
        
    /**
     * Creates an XMLReader from a StAX-based XMLStreamReader.
     * 
     * @param inputStream the InputStream to read from.
     * @return an XMLReader.
     */
    public static XMLReader getXMLReader( InputStream inputStream )
    {
        try
        {
            XMLInputFactory factory = new WstxInputFactory();
        
            XMLStreamReader streamReader = factory.createXMLStreamReader( inputStream );
            
            return new DefaultXMLStreamReader( streamReader );
        }
        catch ( XMLStreamException ex )
        {
            throw new RuntimeException( "Failed to create XMLStreamReader", ex );
        }
    }
}
