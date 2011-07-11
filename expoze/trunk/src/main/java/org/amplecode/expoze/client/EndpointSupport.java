package org.amplecode.expoze.client;

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

import java.io.IOException;

import org.amplecode.staxwax.factory.XMLFactory;
import org.amplecode.staxwax.reader.XMLReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * This class provides support methods for implementing web service endpoints.
 * 
 * @author Lars Helge Overland
 * @version $Id: EndpointSupport.java 121 2008-11-24 23:55:16Z larshelg $
 */
public abstract class EndpointSupport
{
    private static final String URL_SEPARATOR = "/";
    
    /**
     * Return the XML reader with the XML stream retrieved from the given path.
     * 
     * @param baseUrl the base URL.
     * @param path the path to use in the URL from where the XML stream is retrieved.
     * @return an XMLReader.
     */
    protected XMLReader getXmlReader( String baseUrl, String path )
    {
        String url = baseUrl + URL_SEPARATOR + path;
        
        try
        {
            Resource resource = new UrlResource( url );
            
            if ( !resource.exists() )
            {
                throw new IllegalArgumentException( "URL does not exist: " + url );
            }
        
            XMLReader reader = XMLFactory.getXMLReader( resource.getInputStream() );
            
            return reader;
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to load URL", ex );
        }
    }
}
