package org.amplecode.cave.context.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.amplecode.cave.context.ClientContext;
import org.amplecode.cave.context.ClientContextHolder;

/**
 * Servlet filter for storing the client context in HTTP session in between the
 * requests from a client.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ClientContextSessionIntegrationFilter.java 52 2007-12-19 18:32:00Z torgeilo $
 */
public class ClientContextSessionIntegrationFilter
    implements Filter
{
    public static final String KEY = "__" + ClientContextSessionIntegrationFilter.class.getName();

    // -------------------------------------------------------------------------
    // Filter
    // -------------------------------------------------------------------------

    public final void init( FilterConfig config )
        throws ServletException
    {
    }

    public final void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        if ( !(request instanceof HttpServletRequest) )
        {
            throw new IllegalArgumentException( "Requires HttpServletRequest, got " + request.getClass().getName() );
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpSession httpSession;

        ClientContext clientContext;

        // ---------------------------------------------------------------------
        // Fetch
        // ---------------------------------------------------------------------

        httpSession = httpRequest.getSession( false );

        if ( httpSession != null )
        {
            clientContext = (ClientContext) httpSession.getAttribute( KEY );

            if ( clientContext != null )
            {
                ClientContextHolder.setClientContext( clientContext );

                /*
                 * No point in having a reference to the client context. The
                 * object might get replaced.
                 */
                clientContext = null;
            }

            /*
             * Don't want to keep a reference to the session in case it gets
             * invalidated.
             */
            httpSession = null;
        }

        // ---------------------------------------------------------------------
        // Continue
        // ---------------------------------------------------------------------

        chain.doFilter( request, response );

        // ---------------------------------------------------------------------
        // Store
        // ---------------------------------------------------------------------

        clientContext = ClientContextHolder.getClientContext();

        if ( clientContext == null )
        {
            httpSession = httpRequest.getSession( false );

            if ( httpSession != null )
            {
                httpSession.removeAttribute( KEY );
            }
        }
        else
        {
            httpSession = httpRequest.getSession( true );

            httpSession.setAttribute( KEY, clientContext );
        }

        /*
         * Don't want the ClientContextHolder to keep a reference to the client
         * context. The thread is likely to be used by a different client next
         * request.
         */
        ClientContextHolder.removeClientContext();
    }

    public final void destroy()
    {
    }
}
