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

import org.amplecode.expoze.model.Student;
import org.amplecode.staxwax.reader.XMLReader;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class StudentObjectMapper
    extends ObjectMapper<Student>
{
    private final String ELEMENT_NAME = "student";
    private final String COLLECTION_NAME = "students";
    private final String FIELD_ID = "id";
    private final String FIELD_NAME = "name";
    private final String FIELD_ADDRESS = "address";
    private final String FIELD_PHONE_NUMBER = "phoneNumber";
    
    @Override
    public Student getObject( XMLReader reader )
    {
        Student student = new Student();
        
        reader.moveToStartElement( FIELD_ID );
        student.setId( Integer.valueOf( reader.getElementValue() ) );
        
        reader.moveToStartElement( FIELD_NAME );
        student.setName( reader.getElementValue() );
        
        reader.moveToStartElement( FIELD_ADDRESS );
        student.setAddress( reader.getElementValue() );
        
        reader.moveToStartElement( FIELD_PHONE_NUMBER );
        student.setPhoneNumber( Integer.valueOf( reader.getElementValue() ) );
        
        return student;
    }
    
    @Override
    protected String getElementName()
    {
        return ELEMENT_NAME;
    }
    
    @Override
    protected String getCollectionName()
    {
        return COLLECTION_NAME;
    }
}
