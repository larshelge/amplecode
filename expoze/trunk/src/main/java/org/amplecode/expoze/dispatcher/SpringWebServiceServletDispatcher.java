package org.amplecode.expoze.dispatcher;

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

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.amplecode.expoze.configuration.Configuration;
import org.amplecode.expoze.configuration.ConfigurationProvider;
import org.amplecode.expoze.configuration.DefaultConfigurationProvider;
import org.amplecode.expoze.configuration.Request;
import org.amplecode.expoze.util.ContextUtils;
import org.amplecode.expoze.util.Encoder;
import org.amplecode.expoze.util.Formatter;
import org.amplecode.expoze.util.VelocityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import static org.amplecode.expoze.configuration.Request.*;

/**
 * This class is responsible for receiving HTTP requests, reading the configuration
 * definitions and invoking the appropriate method on the requested Spring bean.
 * A built-in request-discovery called getRequests is available. For the document
 * response type, an encoder class and a formatter class are present in the Velocity context.
 * 
 * @author Lars Helge Overland
 * @version $Id: SpringWebServiceServletDispatcher.java 144 2009-06-22 22:16:02Z larshelg $
 */
public class SpringWebServiceServletDispatcher
    extends HttpServlet
{
    private static final Log log = LogFactory.getLog( SpringWebServiceServletDispatcher.class );
    
    private static final String OBJECT_KEY = "object";
    private static final String ENCODER_KEY = "encoder";
    private static final String FORMATTER_KEY = "format";
    private static final String ENCODING = "UTF-8";
    private static final String CONTENT_TYPE_CHART = "image/png";
    private static final String FORMAT_KEY = "format";
    private static final int CHART_WIDTH = 700;
    private static final int CHART_HEIGHT = 500;
        
    private ApplicationContext context;
    
    private Encoder encoder;
    
    private Formatter formatter;
    
    private Configuration config;
    
    private Request configurationRequest;

    // -------------------------------------------------------------------------
    // HttpServlet methods
    // -------------------------------------------------------------------------

    @Override
    public void init( ServletConfig servletConfig )
        throws ServletException
    {
        BeanFactory beanFactory = WebApplicationContextUtils.
            getRequiredWebApplicationContext( servletConfig.getServletContext() );

        GenericApplicationContext genericContext = new GenericWebApplicationContext( new DefaultListableBeanFactory( beanFactory ) );

        // ---------------------------------------------------------------------
        // Register ConfigurationProvider in Spring context
        // ---------------------------------------------------------------------

        BeanDefinition configBean = new RootBeanDefinition( DefaultConfigurationProvider.class );
        genericContext.registerBeanDefinition( ConfigurationProvider.ID, configBean );
        
        genericContext.refresh();
        
        // ---------------------------------------------------------------------
        // Load ConfigurationProvider from Spring context
        // ---------------------------------------------------------------------

        ConfigurationProvider configProvider = (ConfigurationProvider) genericContext.getBean( ConfigurationProvider.ID );
        
        Configuration config = configProvider.getConfiguration();
        
        configurationRequest = configProvider.getConfigurationRequest();
        
        config.addRequest( configurationRequest );
        
        this.context = genericContext;
        this.config = config;
        this.encoder = new Encoder();
        this.formatter = new Formatter();

        // ---------------------------------------------------------------------
        // Load Velocity configuration
        // ---------------------------------------------------------------------

        VelocityUtils.loadConfiguration();
        
        super.init( servletConfig );
        
        log.info( "Configuration successfully loaded" );
    }
    
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )
    {
        doHandle( request, response );
    }
    
    @Override
    public void doPost( HttpServletRequest request, HttpServletResponse response )
    {
        doHandle( request, response );
    }
    
    @Override
    public void doDelete( HttpServletRequest request, HttpServletResponse response )
    {
        doHandle( request, response );
    }
    
    @Override
    public void doPut( HttpServletRequest request, HttpServletResponse response )
    {
        doHandle( request, response );
    }
    
    public void doHandle( HttpServletRequest request, HttpServletResponse response )
    {
        Map<String, String> parameterMap = ContextUtils.getParameterMap( request );
        
        String format = parameterMap.get( FORMAT_KEY );
        
        Context velocityContext = new VelocityContext();

        // ---------------------------------------------------------------------
        // Retrieve request definition from configuration
        // ---------------------------------------------------------------------

        Request requestDefinition = config.getRequest( ContextUtils.getRequestName( request ) );

        // ---------------------------------------------------------------------
        // Check if method type is allowed
        // ---------------------------------------------------------------------

        if ( !requestDefinition.methodTypeIsAllowed( request.getMethod() ) )
        {
            log.warn( "Method type '" + request.getMethod() + "' is not allowed for request '" + requestDefinition.getName() + "'" );
            
            return;
        }
        
        // ---------------------------------------------------------------------
        // Retrieve request bean from Spring context
        // ---------------------------------------------------------------------

        Object bean = context.getBean( requestDefinition.getBean( format ) );
        
        try
        {
            // -----------------------------------------------------------------
            // Invoke request method with arguments
            // -----------------------------------------------------------------

            Method method = bean.getClass().getMethod( requestDefinition.getMethod( format ), requestDefinition.getParameterTypes( format ) );

            String type = requestDefinition.getType( format );

            // -----------------------------------------------------------------
            // Void response
            // -----------------------------------------------------------------            
            
            if ( type.equalsIgnoreCase( TYPE_VOID ) )
            {
                method.invoke( bean, requestDefinition.getArguments( format, parameterMap ) );
            }
            
            // -----------------------------------------------------------------
            // Write method output directly to response
            // -----------------------------------------------------------------            
            
            else if ( type.equalsIgnoreCase( TYPE_FILE ) )
            {
                response.setContentType( requestDefinition.getContentType( format ) );
                
                method.invoke( bean, requestDefinition.getArguments( format, response.getOutputStream(), parameterMap ) );                
            }
            
            // -----------------------------------------------------------------
            // Write JFreeChart to response
            // -----------------------------------------------------------------            
            
            else if ( type.equalsIgnoreCase( TYPE_CHART ) )
            {
                Object result = method.invoke( bean, requestDefinition.getArguments( format, parameterMap )  );

                response.setContentType( CONTENT_TYPE_CHART );
                    
                JFreeChart chart = (JFreeChart) result;
                    
                ChartUtilities.writeChartAsPNG( response.getOutputStream(), chart, CHART_WIDTH, CHART_HEIGHT );
            }
            
            // -----------------------------------------------------------------
            // Merge method output with Velocity and write document to response
            // -----------------------------------------------------------------            
            
            else if ( type.equalsIgnoreCase( TYPE_DOCUMENT ) )
            {
                Object result = method.invoke( bean, requestDefinition.getArguments( format, parameterMap )  );
                
                velocityContext.put( OBJECT_KEY, result );
                velocityContext.put( ENCODER_KEY, encoder );
                velocityContext.put( FORMATTER_KEY, formatter );
    
                response.setContentType( requestDefinition.getContentType( format ) );
                
                response.setCharacterEncoding( ENCODING );
                
                String responseTemplate = requestDefinition.getTemplate( format );
                
                Velocity.mergeTemplate( responseTemplate, ENCODING, velocityContext, response.getWriter() );
            }
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( ex );
        }
    }
}
