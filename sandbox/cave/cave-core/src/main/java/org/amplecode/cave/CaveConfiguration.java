package org.amplecode.cave;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;

/**
 * Configuration providers must have a reference to the ConfigurationManager and
 * use the provided methods to populate the internal configurations.
 * 
 * All SessionFactoryConfig objects are wrapped so that the buildSessionFactory
 * method returns the same sessionFactory as long as it is not closed.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: CaveConfiguration.java 43 2007-10-28 22:36:39Z torgeilo $
 */
public class CaveConfiguration
{
    private Map<String, SessionFactoryConfig> configs;

    private String defaultConfigId;

    private String testConfigId;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public CaveConfiguration()
    {
        configs = new HashMap<String, SessionFactoryConfig>();
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

    public Collection<SessionFactoryConfig> getSessionFactoryConfigs()
    {
        return configs.values();
    }

    public SessionFactoryConfig getSessionFactoryConfig( String id )
    {
        return configs.get( id );
    }

    public SessionFactoryConfig getOrCreateSessionFactoryConfig( String id )
    {
        SessionFactoryConfig config = getSessionFactoryConfig( id );

        if ( config == null )
        {
            config = new SingleSessionFactoryConfig( id );

            configs.put( id, config );
        }

        return config;
    }

    public String getDefaultConfigId()
    {
        return defaultConfigId;
    }

    public void setDefaultConfigId( String id )
        throws IllegalArgumentException
    {
        if ( !configs.containsKey( id ) )
        {
            throw new IllegalArgumentException( "The id doesn't match an existing SessionFactoryConfig" );
        }

        defaultConfigId = id;
    }

    public String getTestConfigId()
    {
        return testConfigId;
    }

    public void setTestConfigId( String id )
        throws IllegalArgumentException
    {
        if ( !configs.containsKey( id ) )
        {
            throw new IllegalArgumentException( "The id doesn't match an existing SessionFactoryConfig" );
        }

        testConfigId = id;
    }

    // -------------------------------------------------------------------------
    // SingleSessionFactoryConfig
    // -------------------------------------------------------------------------

    private class SingleSessionFactoryConfig
        extends SessionFactoryConfig
    {
        private SessionFactory sessionFactory;

        // ---------------------------------------------------------------------
        // Constructor
        // ---------------------------------------------------------------------

        public SingleSessionFactoryConfig( String id )
        {
            super( id );
        }

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------

        @Override
        public synchronized SessionFactory buildSessionFactory()
        {
            if ( sessionFactory == null || sessionFactory.isClosed() )
            {
                sessionFactory = super.buildSessionFactory();
            }

            return sessionFactory;
        }
    }
}
