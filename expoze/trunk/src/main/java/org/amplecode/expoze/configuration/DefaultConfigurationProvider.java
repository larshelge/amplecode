package org.amplecode.expoze.configuration;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultConfigurationProvider.java 144 2009-06-22 22:16:02Z larshelg $
 */
public class DefaultConfigurationProvider
    implements ConfigurationProvider
{
    private static final String CONFIG_FILE_NAME = "dispatcher.xml";
    
    private static final String ALIAS_CONFIGURATION = "expoze";
    private static final String ALIAS_REQUEST = "request";
    private static final String ALIAS_PARAMETER = "parameter";
    private static final String ALIAS_RESPONSE = "response";
    private static final String ALIAS_STRING = "value";
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public DefaultConfigurationProvider()
    {   
    }

    // -------------------------------------------------------------------------
    // ConfigurationProvider implementation
    // -------------------------------------------------------------------------

    public Configuration getConfiguration()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        
        InputStream in = loader.getResourceAsStream( CONFIG_FILE_NAME );

        if ( in == null )
        {
            throw new IllegalStateException( "Configuration file does not exist" );
        }
    
        Configuration object = null;
        
        try
        {
            object = (Configuration) getXStream().fromXML( in );
        }
        catch ( ConversionException ex )
        {
            throw new InvalidConfigurationException( "Invalid configuration", ex );
        }
        finally
        {
            if ( in != null )
            {
                try
                {
                    in.close();
                }
                catch ( IOException ex )
                {
                }
            }
        }
        
        return object;
    }
    
    public void setConfiguration( Configuration configuration, String fileName )
    {
        OutputStream out = null;
        
        try
        {
            out = new FileOutputStream( fileName );
            
            getXStream().toXML( configuration, out );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to write Configuration", ex );
        }
        finally
        {
            if ( out != null )
            {
                try
                {
                    out.close();
                }
                catch ( IOException ex )
                {   
                }
            }
        }
    }
    
    public Request getConfigurationRequest()
    {
        Request request = new Request();

        request.setName( "getRequests" );
        request.setBean( ID );
        request.setMethod( "getConfiguration" );
        request.setTemplate( "requests.vm" );
        request.setContentType( "text/html" );
        
        return request;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private XStream getXStream()
    {
        XStream xStream = new XStream();
        
        xStream.alias( ALIAS_CONFIGURATION, Configuration.class );
        xStream.alias( ALIAS_REQUEST, Request.class );
        xStream.alias( ALIAS_PARAMETER, Parameter.class );
        xStream.alias( ALIAS_RESPONSE, Response.class );
        xStream.alias( ALIAS_STRING, String.class );
        
        return xStream;
    }
}
