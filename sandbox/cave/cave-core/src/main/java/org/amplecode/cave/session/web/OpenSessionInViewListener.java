package org.amplecode.cave.session.web;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.amplecode.cave.CaveClientManager;
import org.amplecode.cave.session.SessionUtils;
import org.hibernate.SessionFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: OpenSessionInViewListener.java 41 2007-10-28 21:09:20Z torgeilo $
 */
public class OpenSessionInViewListener
    implements ServletRequestListener
{
    // -------------------------------------------------------------------------
    // ServletRequestListener
    // -------------------------------------------------------------------------

    public void requestInitialized( ServletRequestEvent event )
    {
    }

    public void requestDestroyed( ServletRequestEvent event )
    {
        SessionFactory sessionFactory = CaveClientManager.getClientSessionFactory();

        SessionUtils.closeSession( sessionFactory.getCurrentSession() );
    }
}
