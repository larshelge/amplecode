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

import java.util.Collection;
import java.util.HashSet;

import org.amplecode.staxwax.reader.XMLReader;

/**
 * This class is responsible for mapping XML to objects (XOM).
 * 
 * @author Lars Helge Overland
 * @version $Id: ObjectMapper.java 118 2008-11-24 23:09:53Z larshelg $
 */
public abstract class ObjectMapper<T>
{
    public abstract T getObject( XMLReader reader );
    
    /**
     * Returns the name of the implementing object's XML element name.
     */
    protected abstract String getElementName();
    
    /**
     * Returns the name of the implementing object's XML collection name.
     */
    protected abstract String getCollectionName();
    
    /**
     * Returns a collection of objects retrieved from the XMLReader.
     * 
     * @param reader the XMLReader.
     * @return a collection of objects retrieved from the XMLReader.
     */
    public Collection<T> getObjects( XMLReader reader )
    {
        Collection<T> objects = new HashSet<T>();
        
        while ( reader.moveToStartElement( getElementName(), getCollectionName() ) )
        {
            objects.add( getObject( reader ) );
        }
        
        return objects;
    }
}
