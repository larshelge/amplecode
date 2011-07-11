package org.amplecode.cave.provider;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.amplecode.cave.CaveConfiguration;
import org.amplecode.cave.SessionFactoryConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: RegexpPropertyConfigProvider.java 48 2007-12-19 15:50:43Z torgeilo $
 */
public class RegexpPropertyConfigProvider
    implements ConfigProvider
{
    private final Log log = LogFactory.getLog( RegexpPropertyConfigProvider.class );

    public static final String TEST_CONFIG = "__test__";

    public static final String ALL_BUT_TEST_CONFIG = "__!test__";

    public static final String INVERTED = "__!__";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CaveConfiguration configuration;

    public void setConfiguration( CaveConfiguration configuration )
    {
        this.configuration = configuration;
    }

    private Map<String, Properties> propertiesMap;

    public void setPropertiesMap( Map<String, Properties> propertiesMap )
    {
        this.propertiesMap = propertiesMap;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public void provide()
        throws Exception
    {
        Collection<SessionFactoryConfig> configs = configuration.getSessionFactoryConfigs();

        for ( Entry<String, Properties> entry : propertiesMap.entrySet() )
        {
            String exp;
            boolean invert = false;

            if ( TEST_CONFIG.equals( entry.getKey() ) )
            {
                exp = configuration.getTestConfigId();
            }
            else if ( ALL_BUT_TEST_CONFIG.equals( entry.getKey() ) )
            {
                exp = configuration.getTestConfigId();
                invert = true;
            }
            else if ( entry.getKey().startsWith( INVERTED ) )
            {
                exp = entry.getKey().substring( INVERTED.length() );
                invert = true;
            }
            else
            {
                exp = entry.getKey();
            }

            Pattern pattern = Pattern.compile( exp );

            for ( SessionFactoryConfig config : configs )
            {
                if ( pattern.matcher( config.getId() ).matches() ^ invert )
                {
                    log.debug( "Expression '" + (invert ? "[NOT]" : "") + exp + "' matches '" + config.getId() + "'" );

                    config.getConfiguration().addProperties( entry.getValue() );
                }
                else
                {
                    log.debug( "Expression '" + (invert ? "[NOT]" : "") + exp + "' doesn't match '" + config.getId()
                        + "'" );
                }
            }
        }
    }
}
