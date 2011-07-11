package org.amplecode.staxwax.writer;

import javax.xml.stream.XMLStreamWriter;


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
 * @version $Id: DefaultIndentingXMLStreamWriter.java 114 2008-11-23 20:17:23Z larshelg $
 */
public class DefaultLineBreakingXMLStreamWriter
    implements XMLWriter
{
    private static final String LINE_BREAK = "\n";
    
    private XMLWriter writer;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public DefaultLineBreakingXMLStreamWriter( XMLWriter writer )
    {
        this.writer = writer;
    }

    // -------------------------------------------------------------------------
    // XMLWriter implementation
    // -------------------------------------------------------------------------

    public void openDocument( String encoding, String version )
    {
        writer.openDocument( encoding, version );
        
        writer.writeCharacters( LINE_BREAK );
    }

    public void openElement( String name )
    {
        writer.openElement( name );
        
        writer.writeCharacters( LINE_BREAK );
    }

    public void openElement( String name, String... attributeNameValuePairs )
    {
        writer.openElement( name, attributeNameValuePairs );
        
        writer.writeCharacters( LINE_BREAK );
    }

    public void writeElement( String name, String value )
    {
        writer.writeElement( name, value );
        
        writer.writeCharacters( LINE_BREAK );
    }

    public void writeElement( String name, String value, String... attributeNameValuePairs )
    {
        writer.writeElement( name, value, attributeNameValuePairs );
        
        writer.writeCharacters( LINE_BREAK );
    }
    
    public void writeCharacters( String characters )
    {
        writer.writeCharacters( characters );
    }
    
    public void writeCData( String cData )
    {
        writer.writeCData( cData );
    }

    public XMLStreamWriter getXmlStreamWriter()
    {
        return writer.getXmlStreamWriter();
    }
    
    public void closeElement()
    {
        writer.closeElement();
        
        writer.writeCharacters( LINE_BREAK );
    }
    
    public void closeDocument()
    {
        writer.closeDocument();
    }

    public void closeWriter()
    {
        writer.closeWriter();
    }
}
