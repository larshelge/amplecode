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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the Expoze dispatcher configuration.
 * 
 * @author Lars Helge Overland
 * @version $Id: Configuration.java 128 2008-12-02 14:09:56Z larshelg $
 */
public class Configuration
    implements Serializable
{
    private Set<Request> requests = new HashSet<Request>();
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Configuration()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Retrieves the Request definition from the configuration with the given name.
     * 
     * @param name the request name.
     * @return the Request.
     */
    public Request getRequest( String name )
    {
        for ( Request request : requests )
        {
            if ( request.getName().equals( name ) )
            {
                return request;
            }
        }
        
        throw new RuntimeException( "Request definition does not exist: " + name );
    }
    
    /**
     * Adds a Request definition to the configuration.
     * 
     * @param request the Request to add.
     */
    public void addRequest( Request request )
    {
        requests.add( request );
    }
    
    /**
     * Returns all Requests sorted ascending on the request property.
     * 
     * @return list of Requests.
     */
    public List<Request> getSortedRequests()
    {
        List<Request> requestList = new ArrayList<Request>( requests );
        
        Collections.sort( requestList );
        
        return requestList;
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Set<Request> getRequests()
    {
        return requests;
    }

    public void setRequests( Set<Request> requests )
    {
        this.requests = requests;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return requests.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        Configuration other = (Configuration) object;
        
        return requests.equals( other.getRequests() );
    }
    
    @Override
    public String toString()
    {
        return requests.toString();
    }
}
