package org.amplecode.cave.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.context.CurrentSessionContext;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: CaveSessionContext.java 37 2007-10-04 18:51:13Z torgeilo $
 */
public class CaveSessionContext
    implements CurrentSessionContext
{
    private final Log log = LogFactory.getLog( CaveSessionContext.class );

    protected final SessionFactoryImplementor sessionFactory;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public CaveSessionContext( SessionFactoryImplementor sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------------------------------------------------------
    // CurrentSessionContext
    // -------------------------------------------------------------------------

    private ThreadLocal<Session> currentSession = new ThreadLocal<Session>();

    /**
     * Returns thread local sessions. If there is no session a new one is
     * created.
     */
    public final Session currentSession()
        throws HibernateException
    {
        Session session = currentSession.get();

        if ( session == null )
        {
            log.debug( "Creating new Hibernate session" );

            session = openSession();

            session = wrapSession( session );

            currentSession.set( session );
        }

        return session;
    }

    /**
     * Can be overridden to provide sessions opened in different ways.
     * 
     * @return a new session instance.
     */
    protected Session openSession()
    {
        return sessionFactory.openSession();
    }

    /**
     * Wraps the session in a SessionWrapper proxy.
     */
    private Session wrapSession( Session session )
    {
        SessionProxyHandler wrapper = new SessionProxyHandler( session );

        Session proxy = (Session) Proxy.newProxyInstance( Session.class.getClassLoader(), new Class[] { Session.class,
            SessionProxy.class }, wrapper );

        return proxy;
    }

    /**
     * Does the actual closing of the session. It is called by the
     * {@link SessionProxyHandler} when someone calls close() on the proxy.
     */
    private void closeSession( SessionProxyHandler sessionProxyHandler )
    {
        /*
         * Check that the session is closed by the right thread.
         */
        Session providedSession = sessionProxyHandler.getRealSession();

        Session threadLocalSession = ((SessionProxy) currentSession.get()).getRealSession();

        currentSession.remove();

        if ( !providedSession.equals( threadLocalSession ) )
        {
            throw new IllegalStateException( "Hibernate session not opened and closed from the same thread" );
        }

        /*
         * Close session
         */

        log.debug( "Closing Hibernate session" );

        try
        {
            providedSession.close();
        }
        catch ( HibernateException e )
        {
            log.error( "Failed to close session", e );
        }
        catch ( Throwable t )
        {
            log.error( "Caught unexpected exception while closing session", t );
        }
    }

    // -------------------------------------------------------------------------
    // SessionWrapper
    // -------------------------------------------------------------------------

    /**
     * Interface for accessing the real session from the proxy.
     */
    private interface SessionProxy
    {
        Session getRealSession();
    }

    /**
     * Proxy handler which makes sure that all calls to {@code session.close()}
     * are redirected to {@link #closeSession(Session)}.
     */
    private class SessionProxyHandler
        implements InvocationHandler, SessionProxy
    {
        private final Session wrappedSession;

        // ---------------------------------------------------------------------
        // Constructor
        // ---------------------------------------------------------------------

        public SessionProxyHandler( Session wrappedSession )
        {
            this.wrappedSession = wrappedSession;
        }

        // ---------------------------------------------------------------------
        // Getter
        // ---------------------------------------------------------------------

        public Session getRealSession()
        {
            return wrappedSession;
        }

        // ---------------------------------------------------------------------
        // InvocationHandler
        // ---------------------------------------------------------------------

        public Object invoke( Object proxy, Method method, Object[] args )
            throws Throwable
        {
            if ( "close".equals( method.getName() ) )
            {
                closeSession( this );

                return null;
            }
            else if ( "getRealSession".equals( method.getName() ) )
            {
                return getRealSession();
            }
            else
            {
                return method.invoke( wrappedSession, args );
            }
        }
    }
}
