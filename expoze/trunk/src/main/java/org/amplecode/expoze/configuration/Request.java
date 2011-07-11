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

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a request definition in the Expoze dispatcher configuration.
 * 
 * @author Lars Helge Overland
 * @version $Id: Request.java 144 2009-06-22 22:16:02Z larshelg $
 */
public class Request
    implements Serializable, Comparable<Request>
{
    public static final String TYPE_DOCUMENT = "document";
    public static final String TYPE_CHART = "chart";
    public static final String TYPE_FILE = "file";
    public static final String TYPE_VOID = "void";
    
    private static final long serialVersionUID = 7526472295622776147L;

    private static final String DEFAULT_TYPE = "document";
    private static final String DEFAULT_CONTENT_TYPE = "text/xml";
    
    /**
     * Mandatory.
     */
    private String name;

    /**
     * Optional. Defaults to "document".
     */
    private String type;
    
    /**
     * Mandatory.
     */
    private String bean;
    
    /**
     * Mandatory.
     */
    private String method;
    
    /**
     * Optional.
     */
    private List<String> methodTypes = new ArrayList<String>();
    
    /**
     * Optional. Must be set in order to recognize input parameters.
     */
    private List<Parameter> parameters = new ArrayList<Parameter>();

    /**
     * Mandatory for document. Optional for chart and file.
     */
    private String template;
    
    /**
     * Optional. Defaults to "text/xml".
     */
    private String contentType;

    /**
     * Optional. Will override settings from this class.
     */
    private List<Response> responses = new ArrayList<Response>();
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Request()
    {
    }

    // -------------------------------------------------------------------------
    // Parameter/argument logic
    // -------------------------------------------------------------------------

    /**
     * Indicates whether the method type is among the allowed method types defined
     * in the configuration. All types are allowed if none is defined.
     */
    public boolean methodTypeIsAllowed( String methodType )
    {
        if ( methodTypes == null || methodTypes.size() == 0 )
        {
            return true;
        }
        
        if ( methodType == null )
        {
            return false;
        }
        
        for ( String allowedMethodType : methodTypes )
        {
            if ( allowedMethodType.toLowerCase().equals( methodType.toLowerCase() ) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Retrieves the Class types of the parameters in the request.
     * 
     * @return a Class array of parameter types.
     */
    public Class<?>[] getParameterTypes( String format )
    {
        List<Parameter> parameters = getParameters( format );
        
        if ( parameters == null )
        {
            return new Class<?>[0];
        }
        
        Class<?>[] parameterTypes = new Class<?>[ parameters.size() ];
        
        for ( int i = 0; i < parameters.size(); i++ )
        {
            parameterTypes[i] = parameters.get( i ).getType();
        }
        
        return parameterTypes;
    }
    
    /**
     * Retrieves the names of the parameters in the request.
     * 
     * @return a String array of parameter names.
     */
    public String[] getParameterNames( String format )
    {
        List<Parameter> parameters = getParameters( format );
        
        if ( parameters == null )
        {
            return new String[0];
        }
        
        String[] parameterNames = new String[ parameters.size() ];
        
        for ( int i = 0; i < parameters.size(); i++ )
        {
            parameterNames[i] = parameters.get( i ).getName();
        }
        
        return parameterNames;
    }
    
    /**
     * Gets the arguments in the request. Inserts the give outputStream to index 0.
     * 
     * @param outputStream the OutputStream to add to the argument array.
     * @param parameterMap a map with parameter name as key and parameter type
     *        as value.
     *        
     * @return an Object array with request arguments.
     */
    public Object[] getArguments( String format, OutputStream outputStream, Map<String, String> parameterMap )
    {
        Object[] arguments = getArguments( format, parameterMap );
        
        arguments[0] = outputStream;
        
        return arguments;
    }
    
    /**
     * Get the arguments in the request.
     * 
     * @param parameterMap a map with parameter name as key and parameter type
     *        as value.
     *        
     * @return an Object array with request arguments.
     */
    public Object[] getArguments( String format, Map<String, String> parameterMap )
    {
        String[] parameterNames = getParameterNames( format );
        Class<?>[] parameterTypes = getParameterTypes( format );
        
        Object[] arguments = new Object[ parameterNames.length ];
        
        for ( int i = 0; i < parameterNames.length; i++ )
        {
            arguments[i] = cast( parameterMap.get( parameterNames[i] ), parameterTypes[i] );
        }
        
        return arguments;
    }

    // -------------------------------------------------------------------------
    // Property logic
    // -------------------------------------------------------------------------

    /**
     * Looks for a type set in a Response for the provided format, then a type
     * set in the Request, then the default type.
     * 
     * @param format the format.
     * @return the type.
     */
    public String getType( String format )
    {
        if ( responseExists( format ) && getResponse( format ).getType() != null )
        {
            return getResponse( format ).getType();
        }
        
        return type != null ? type : DEFAULT_TYPE;
    }

    /**
     * Looks for a bean set in a Response for the provided format, then a bean
     * set in the Request.
     * 
     * @param format the format.
     * @return the bean.
     * @throws InvalidConfigurationException if no bean is set.
     */
    public String getBean( String format )
    {
        if ( responseExists( format ) && getResponse( format ).getBean() != null )
        {
            return getResponse( format ).getBean();
        }
        
        if ( bean == null )
        {
            throw new InvalidConfigurationException( "Bean must be defined for request: " + name );
        }
        
        return bean;
    }

    /**
     * Looks for a method set in a Response for the provided format, then a method
     * set in the Request.
     * 
     * @param format the format.
     * @return the method.
     * @throws InvalidConfigurationException if no method is set.
     */
    public String getMethod( String format )
    {
        if ( responseExists( format ) && getResponse( format ).getMethod() != null )
        {
            return getResponse( format ).getMethod();
        }
        
        if ( method == null )
        {
            throw new InvalidConfigurationException( "Method must be defined for request: " + name );
        }
        
        return method;
    }
    
    /**
     * Looks for a list of Parameters set in a Response for the provided format,
     * then a  list of Parameters set in the Request.
     * 
     * @param format the format.
     * @return the list of Parameters.
     */
    public List<Parameter> getParameters( String format )
    {
        if ( responseExists( format ) && getResponse( format ).getParameters() != null )
        {
            return getResponse( format ).getParameters();
        }
        
        return parameters;
    }

    /**
     * Looks for a template set in a Response for the provided format, then a template
     * set in the Request.
     * 
     * @param format the format.
     * @return the template.
     * @throws InvalidConfigurationException if no template is set.
     */
    public String getTemplate( String format )
    {
        if ( responseExists( format ) && getResponse( format ).getTemplate() != null )
        {
            return getResponse( format ).getTemplate();
        }
        
        return template;
    }

    /**
     * Looks for a content type set in a Response for the provided format, then 
     * a content type set in the Request, then the default content type.
     * 
     * @param format the format.
     * @return type the type.
     */
    public String getContentType( String format )
    {
        if ( responseExists( format ) && getResponse( format ).getContentType() != null )
        {
            return getResponse( format ).getContentType();
        }
        
        return contentType != null ? contentType : DEFAULT_CONTENT_TYPE;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns the response with the provided format.
     * 
     * @param format the format.
     * @return the Response.
     */
    private Response getResponse( String format )
    {
        if ( format != null )
        {
            for ( Response response : responses )
            {
                if ( response.getFormat().equalsIgnoreCase( format ) )
                {
                    return response;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns true if a Response for the provided format exists, false otherwise.
     * 
     * @param format the format.
     * @return true if a Response for the provided format exists.
     */
    private boolean responseExists( String format )
    {
        if ( format != null )
        {
            for ( Response response : responses )
            {
                if ( response.getFormat().equalsIgnoreCase( format ) )
                {
                    return true;
                }
            }
        }
        
        return false;
    }
        
    /**
     * Casts the input value argument to the Class type of the class argument.
     * 
     * @param value the value to cast.
     * @param clazz the Class type to cast the value to.
     * @return the casted value.
     */
    private Object cast( String value, Class<?> clazz )
    {
        if ( value == null )
        {
            return null;
        }
        if ( clazz.equals( String.class ) )
        {
            return value;
        }
        if ( clazz.equals( Integer.class ) || clazz.equals( int.class ) )
        {
            return Integer.valueOf( value );
        }
        if ( clazz.equals( Short.class ) || clazz.equals( short.class ) )
        {
            return Short.valueOf( value );
        }
        if ( clazz.equals( Long.class ) || clazz.equals( long.class ) )
        {
            return Long.valueOf( value );
        }
        if ( clazz.equals( Float.class ) || clazz.equals( float.class ) )
        {
            return Float.valueOf( value );
        }
        if ( clazz.equals( Double.class ) || clazz.equals( double.class ) )
        {
            return Double.valueOf( value );
        }
        if ( clazz.equals( Byte.class ) || clazz.equals( byte.class ) )
        {
            return Byte.valueOf( value );
        }
        
        return value;
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getBean()
    {
        return bean;
    }

    public void setBean( String bean )
    {
        this.bean = bean;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod( String method )
    {
        this.method = method;
    }

    public List<String> getMethodTypes()
    {
        return methodTypes;
    }

    public void setMethodTypes( List<String> methodTypes )
    {
        this.methodTypes = methodTypes;
    }
    
    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters( List<Parameter> parameters )
    {
        this.parameters = parameters;
    }

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate( String template )
    {
        this.template = template;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType( String contentType )
    {
        this.contentType = contentType;
    }

    public List<Response> getResponses()
    {
        return responses;
    }

    public void setResponses( List<Response> responses )
    {
        this.responses = responses;
    }
    
    // -------------------------------------------------------------------------
    // Comparable implementation
    // -------------------------------------------------------------------------

    public int compareTo( Request other )
    {
        if ( other == null )
        {
            return -1;
        }
        
        return name.compareTo( other.name );
    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
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
        
        Request other = (Request) object;

        return name.equals( other.name );
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
