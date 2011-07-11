package org.amplecode.expoze.util;

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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Support class for managing HTTPServletRequests and HTTPServletResponses.
 * 
 * @author Lars Helge Overland
 * @version $Id: ContextUtils.java 128 2008-12-02 14:09:56Z larshelg $
 */
public class ContextUtils
{
    private static final String URL_PATTERN = ".";
    private static final String URL_SEPARATOR = "/";
    
    /**
     * Retrieves a map with request parameters and corresponding values from the
     * HTTPServletRequest.
     * 
     * @param request the HTTPServletRequest.
     * @return a map with request parameters and corresponding values.
     */
    @SuppressWarnings( "unchecked" )
    public static Map<String, String> getParameterMap( HttpServletRequest request )
    {
        Enumeration<String> enumeration = request.getParameterNames();
        
        Map<String, String> params = new HashMap<String, String>();
        
        while ( enumeration.hasMoreElements() )
        {
            String name = enumeration.nextElement();
            
            params.put( name, request.getParameter( name ) );
        }
        
        return params;
    }
    
    /**
     * Retrieves the Expoze request name from the HTTPServletRequest.
     * 
     * @param request the HTTPServletRequest.
     * @return the request name.
     */
    public static String getRequestName( HttpServletRequest request )
    {
        String query = request.getRequestURI();
        
        int startIndex = query.lastIndexOf( URL_SEPARATOR ) + 1;
        int endIndex = query.lastIndexOf( URL_PATTERN );
        
        if ( startIndex == 0 || endIndex == -1 )
        {
            throw new RuntimeException( "Query string is not on the correct format: " + query );
        }
        
        return query.substring( startIndex, endIndex ); 
    }
}
