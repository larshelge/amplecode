package org.amplecode.cave.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: SessionUtils.java 41 2007-10-28 21:09:20Z torgeilo $
 */
public class SessionUtils
{
    private static final Log log = LogFactory.getLog( SessionUtils.class );

    /**
     * Closes a session and logs any exceptions caught.
     */
    public static void closeSession( Session session )
    {
        if ( session != null && session.isOpen() )
        {
            try
            {
                session.close();
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
    }
}
