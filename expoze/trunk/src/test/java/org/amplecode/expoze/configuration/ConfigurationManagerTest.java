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

import junit.framework.TestCase;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ConfigurationManagerTest
    extends TestCase
{
    private ConfigurationProvider configurationProvider;
    
    private Configuration configA;
    
    private Request requestB;
    private Request requestA;
    
    private Parameter parameterA;
    
    private Response responseA;
    private Response responseB;
    
    @Override
    public void setUp()
    {
        configurationProvider = new DefaultConfigurationProvider();

        parameterA = new Parameter();
        parameterA.setType( Integer.class );
        parameterA.setName( "id" );
        
        responseA = new Response();
        responseA.setFormat( "json" );
        responseA.setTemplate( "dataElementsJson.vm" );
        responseA.setContentType( "text/x-json" );
        
        responseB = new Response();
        responseB.setFormat( "xml" );
        responseB.setTemplate( "dataElementsXml.vm" );
        responseB.setContentType( "text/xml" );

        requestA = new Request();
        requestA.setName( "getDataElement" );
        requestA.setType( "document" );
        requestA.setBean( "org.hisp.dhis.dataelement.DataElementService" );
        requestA.setMethod( "getDataElement" );
        requestA.getMethodTypes().add( "get" );
        requestA.getMethodTypes().add( "post" );
        requestA.getParameters().add( parameterA );
        requestA.setTemplate( "dataElement.vm" );
        requestA.setContentType( "text/xml" );
                   
        requestB = new Request();
        requestB.setName( "getDataElements" );
        requestB.setBean( "org.hisp.dhis.dataelement.DataElementService" );
        requestB.setMethod( "getAllDataElements" );
        requestB.setTemplate( "dataElements.vm" );
        requestB.getResponses().add( responseA );
        requestB.getResponses().add( responseB );
             
        configA = new Configuration();
        configA.getRequests().add( requestB );
        configA.getRequests().add( requestA );
    }
    
    public void testGetConfiguration()
    {
        Configuration actual = configurationProvider.getConfiguration();
        assertEquals( configA, actual );
    }
    
    public void testGetRequest()
    {
        Configuration config = configurationProvider.getConfiguration();

        Request actual = config.getRequest( "getDataElement" );
        assertEquals( requestA, actual );
        assertEquals( requestA.getMethodTypes(), actual.getMethodTypes() );
        assertEquals( requestA.getParameters(), actual.getParameters() );
        
        actual = config.getRequest( "getDataElements" );
        assertEquals( requestB, actual );
        assertEquals( requestB.getResponses(), actual.getResponses() );        
    }
}
