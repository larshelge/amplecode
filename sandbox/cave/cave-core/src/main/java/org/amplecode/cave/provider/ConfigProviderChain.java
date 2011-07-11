package org.amplecode.cave.provider;

import java.util.List;

import org.amplecode.cave.CaveConfiguration;

/**
 * A ConfigProvider which delegates the loading to a list of ConfigProvider. If
 * the ConfigurationManager is set, it will be given to all the ConfigProvider
 * in the chain.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ConfigProviderChain.java 45 2007-12-19 15:36:52Z torgeilo $
 */
public class ConfigProviderChain
    implements ConfigProvider
{
    private List<ConfigProvider> providers;

    private CaveConfiguration configuration;

    // -------------------------------------------------------------------------
    // Setter
    // -------------------------------------------------------------------------

    public void setProviders( List<ConfigProvider> providers )
    {
        this.providers = providers;
    }

    // -------------------------------------------------------------------------
    // ConfigurationLoader interface
    // -------------------------------------------------------------------------

    public void setConfiguration( CaveConfiguration configuration )
    {
        this.configuration = configuration;
    }

    public void provide()
        throws Exception
    {
        if ( providers != null )
        {
            for ( ConfigProvider configurationLoader : providers )
            {
                if ( configuration != null )
                {
                    configurationLoader.setConfiguration( configuration );
                }

                configurationLoader.provide();
            }
        }
    }
}
