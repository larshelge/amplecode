package org.amplecode.cave;

import org.amplecode.cave.session.CaveSessionContext;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

/**
 * Holds a Hibernate configuration object and some metadata. Used to assemble a
 * complete session factory configuration, distinguishable from other session
 * factory configurations.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SessionFactoryConfig.java 43 2007-10-28 22:36:39Z torgeilo $
 */
public class SessionFactoryConfig
{
    private final String id;

    private Configuration configuration;

    private String name;

    private String description;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public SessionFactoryConfig( String id )
    {
        this.id = id;

        configuration = new Configuration();

        configuration.setProperty( Environment.CURRENT_SESSION_CONTEXT_CLASS, CaveSessionContext.class.getName() );
    }

    // -------------------------------------------------------------------------
    // Build session factory
    // -------------------------------------------------------------------------

    public SessionFactory buildSessionFactory()
    {
        return configuration.buildSessionFactory();
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public String getId()
    {
        return id;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration( Configuration configuration )
    {
        this.configuration = configuration;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
}
