package org.amplecode.cave;

import org.amplecode.cave.context.ClientContext;
import org.amplecode.cave.context.ClientContextHolder;
import org.hibernate.SessionFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: CaveClientManager.java 43 2007-10-28 22:36:39Z torgeilo $
 */
public abstract class CaveClientManager
{
    public static final String CONTEXT_KEY = "__session_factory_config";

    /**
     * Returns the session factory in the client context. Returns null if there
     * is no session factory.
     */
    public static SessionFactory getClientSessionFactory()
    {
        SessionFactoryConfig config = getClientSessionFactoryConfig();

        if ( config == null )
        {
            return null;
        }

        return config.buildSessionFactory();
    }

    public static SessionFactoryConfig getClientSessionFactoryConfig()
    {
        ClientContext clientContext = ClientContextHolder.getClientContext();

        return (SessionFactoryConfig) clientContext.getAttribute( CONTEXT_KEY );
    }

    /**
     * Sets the client session factory configuration.
     * 
     * @throws IllegalStateException if there is already a session factory
     *         configuration in the client context.
     */
    public static void setClientSessionFactoryConfig( SessionFactoryConfig sessionFactoryConfig )
        throws IllegalStateException
    {
        ClientContext clientContext = ClientContextHolder.getClientContext();

        SessionFactoryConfig oldConfig = (SessionFactoryConfig) clientContext.getAttribute( CONTEXT_KEY );

        if ( oldConfig != null )
        {
            throw new IllegalStateException( "There is already a SessionFactoryConfig reference in the ClientContext" );
        }

        clientContext.setAttribute( CONTEXT_KEY, sessionFactoryConfig );
    }

    /**
     * Removes the session factory from the client context.
     */
    public static void removeClientSessionFactoryConfig()
    {
        ClientContext clientContext = ClientContextHolder.getClientContext( false );

        if ( clientContext != null )
        {
            clientContext.removeAttribute( CONTEXT_KEY );
        }
    }
}
