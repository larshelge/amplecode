package org.amplecode.cave.context;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ClientContextHolder.java 51 2007-12-19 18:12:01Z torgeilo $
 */
public abstract class ClientContextHolder
{
    private static final Log log = LogFactory.getLog( ClientContextHolder.class );

    private static Class<? extends ClientContext> clientContextClass = DefaultClientContext.class;

    private static ClientContextHolderStrategy strategy = new ThreadLocalHolderStrategy();

    // -------------------------------------------------------------------------
    // Configuration methods
    // -------------------------------------------------------------------------

    /**
     * Sets the ClientContextHolderStrategy class/object to use to hold the
     * ClientContexts. The default strategy is the
     * {@link ThreadLocalHolderStrategy}.
     * 
     * @throws IllegalArgumentException if the argument is null or not
     *         assignable from {@link ClientContextHolderStrategy}.
     * @throws RuntimeException if unable to instantiate the class.
     */
    public static void setHolderStrategyClass( Class<? extends ClientContextHolderStrategy> clazz )
    {
        if ( clazz == null || !clazz.isAssignableFrom( ClientContextHolderStrategy.class ) )
        {
            throw new IllegalArgumentException( "Argument must be assignable from ClientContextHolderStrategy, got: "
                + clazz );
        }

        strategy = (ClientContextHolderStrategy) newInstance( clazz );

        log.debug( "ClientContextHolder strategy changed to " + clazz.getName() );
    }

    /**
     * Sets the ClientContextHolderStrategy to use to hold the ClientContexts.
     * The default strategy is the {@link ThreadLocalHolderStrategy}.
     * 
     * @throws IllegalArgumentException if the argument is null.
     */
    public static void setHolderStrategy( ClientContextHolderStrategy holderStrategy )
    {
        if ( holderStrategy == null )
        {
            throw new IllegalArgumentException( "Argument mustnot be null" );
        }

        strategy = holderStrategy;

        log.debug( "ClientContextHolder strategy changed to " + strategy.getClass().getName() );
    }

    /**
     * Returns the ClientContextHolderStrategy class in use.
     */
    public static Class<? extends ClientContextHolderStrategy> getHolderStrategyClass()
    {
        return strategy.getClass();
    }

    /**
     * Sets the ClientContext class to instantiate if there is no ClientContext.
     */
    public static void setClientContextClass( Class<? extends ClientContext> clazz )
    {
        if ( clazz == null || !clazz.isAssignableFrom( ClientContext.class ) )
        {
            throw new IllegalArgumentException( "Argument must be assignable from ClientContext" );
        }

        clientContextClass = clazz;
    }

    /**
     * Returns the ClientContext class in use.
     */
    public static Class<? extends ClientContext> getClientContextClass()
    {
        return clientContextClass;
    }

    // -------------------------------------------------------------------------
    // Holder methods
    // -------------------------------------------------------------------------

    /**
     * Sets the ClientContext.
     */
    public static void setClientContext( ClientContext clientContext )
    {
        strategy.setClientContext( clientContext );
    }

    /**
     * Returns the ClientContext. A new instance is created if none exists.
     */
    public static ClientContext getClientContext()
    {
        return getClientContext( true );
    }

    /**
     * Returns the ClientContext. A new instance is created if none exists and
     * the create argument is true.
     */
    public static ClientContext getClientContext( boolean create )
    {
        ClientContext clientContext = strategy.getClientContext();

        if ( clientContext == null && create )
        {
            clientContext = (ClientContext) newInstance( clientContextClass );

            strategy.setClientContext( clientContext );
        }

        return clientContext;
    }

    /**
     * Removes the ClientContext.
     */
    public static void removeClientContext()
    {
        strategy.removeClientContext();
    }

    // -------------------------------------------------------------------------
    // Class to object
    // -------------------------------------------------------------------------

    public static Object newInstance( Class clazz )
    {
        try
        {
            Constructor constructor = clazz.getConstructor( new Class[] {} );
            return constructor.newInstance( new Object[] {} );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Failed to instantiate " + clazz.getName(), e );
        }
    }
}
