package org.amplecode.cave.proxy;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean for simple distribution of the session factory proxy from
 * {@link SessionFactoryProxyFactory} in a Spring application.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SessionFactoryProxyFactoryBean.java 33 2007-10-03 18:54:15Z torgeilo $
 */
public class SessionFactoryProxyFactoryBean
    implements FactoryBean
{
    public Object getObject()
        throws Exception
    {
        return SessionFactoryProxyFactory.getProxy();
    }

    public Class getObjectType()
    {
        return SessionFactory.class;
    }

    public boolean isSingleton()
    {
        return true;
    }
}
