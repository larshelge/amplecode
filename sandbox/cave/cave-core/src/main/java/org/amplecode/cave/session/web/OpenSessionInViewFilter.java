package org.amplecode.cave.session.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.amplecode.cave.CaveClientManager;
import org.amplecode.cave.session.SessionUtils;
import org.hibernate.SessionFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: OpenSessionInViewFilter.java 41 2007-10-28 21:09:20Z torgeilo $
 */
public class OpenSessionInViewFilter
    implements Filter
{
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
        chain.doFilter( request, response );

        SessionFactory sessionFactory = CaveClientManager.getClientSessionFactory();

        SessionUtils.closeSession( sessionFactory.getCurrentSession() );
    }

    public final void destroy()
    {
    }
}
