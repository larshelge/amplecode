package org.amplecode.cave.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.amplecode.cave.CaveClientManager;
import org.amplecode.cave.context.ClientContext;
import org.hibernate.SessionFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: SessionFactoryProxyFactory.java 41 2007-10-28 21:09:20Z torgeilo $
 */
public class SessionFactoryProxyFactory
{
    private static SessionFactory proxy;

    /**
     * Returns a {@code SessionFactory} proxy which redirects all calls to the
     * actual {@code SessionFactory} associated with the {@link ClientContext}.
     * 
     * @see SessionFactoryUtils#getClientContextSessionFactory()
     */
    public static SessionFactory getProxy()
    {
        if ( proxy == null )
        {
            SessionFactoryHandler handler = new SessionFactoryHandler();

            proxy = (SessionFactory) Proxy.newProxyInstance( SessionFactory.class.getClassLoader(),
                new Class[] { SessionFactory.class }, handler );
        }

        return proxy;
    }

    // -------------------------------------------------------------------------
    // InvocationHandler
    // -------------------------------------------------------------------------

    private static class SessionFactoryHandler
        implements InvocationHandler
    {
        public Object invoke( Object proxy, Method method, Object[] args )
            throws Throwable
        {
            SessionFactory sessionFactory = CaveClientManager.getClientSessionFactory();

            return method.invoke( sessionFactory, args );
        }
    }
}
