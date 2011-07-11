package org.amplecode.cave.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.amplecode.cave.CaveConfiguration;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: RegexpPropertyConfigProviderTest.java 48 2007-12-19 15:50:43Z torgeilo $
 */
public class RegexpPropertyConfigProviderTest
{
    @Test
    public void test()
        throws Exception
    {
        CaveConfiguration configuration = new CaveConfiguration();
        configuration.getOrCreateSessionFactoryConfig( "default" );
        configuration.getOrCreateSessionFactoryConfig( "defaul" );
        configuration.getOrCreateSessionFactoryConfig( "_default" );
        configuration.setDefaultConfigId( "defaul" );
        configuration.setTestConfigId( "default" );

        Properties p0 = new Properties();
        p0.setProperty( "p0k0", "p0v0" );
        p0.setProperty( "p0k1", "p0v1" );

        Properties p1 = new Properties();
        p1.setProperty( "p1k0", "p1v0" );
        p1.setProperty( "p1k1", "p1v1" );

        Properties p2 = new Properties();
        p2.setProperty( "p2k0", "p2v0" );
        p2.setProperty( "p2k1", "p2v1" );

        Properties p3 = new Properties();
        p3.setProperty( "p3k0", "p3v0" );
        p3.setProperty( "p3k1", "p3v1" );

        Properties p4 = new Properties();
        p4.setProperty( "p4k0", "p4v0" );
        p4.setProperty( "p4k1", "p4v1" );

        Map<String, Properties> propertiesMap = new HashMap<String, Properties>();
        propertiesMap.put( RegexpPropertyConfigProvider.TEST_CONFIG, p0 );
        propertiesMap.put( RegexpPropertyConfigProvider.ALL_BUT_TEST_CONFIG, p1 );
        propertiesMap.put( "d.*", p2 );
        propertiesMap.put( ".*", p3 );
        propertiesMap.put( "no match", p4 );

        RegexpPropertyConfigProvider provider = new RegexpPropertyConfigProvider();
        provider.setConfiguration( configuration );
        provider.setPropertiesMap( propertiesMap );

        provider.provide();

        Configuration testConfig = configuration.getSessionFactoryConfig( "default" ).getConfiguration();
        assertEquals( "p0v0", testConfig.getProperty( "p0k0" ) );
        assertEquals( "p0v1", testConfig.getProperty( "p0k1" ) );
        assertNull( testConfig.getProperty( "p1k0" ) );
        assertNull( testConfig.getProperty( "p1k1" ) );
        assertEquals( "p2v0", testConfig.getProperty( "p2k0" ) );
        assertEquals( "p2v1", testConfig.getProperty( "p2k1" ) );
        assertEquals( "p3v0", testConfig.getProperty( "p3k0" ) );
        assertEquals( "p3v1", testConfig.getProperty( "p3k1" ) );
        assertNull( testConfig.getProperty( "p4k0" ) );
        assertNull( testConfig.getProperty( "p4k1" ) );

        Configuration defaultConfig = configuration.getSessionFactoryConfig( "defaul" ).getConfiguration();
        assertNull( defaultConfig.getProperty( "p0k0" ) );
        assertNull( defaultConfig.getProperty( "p0k1" ) );
        assertEquals( "p1v0", defaultConfig.getProperty( "p1k0" ) );
        assertEquals( "p1v1", defaultConfig.getProperty( "p1k1" ) );
        assertEquals( "p2v0", defaultConfig.getProperty( "p2k0" ) );
        assertEquals( "p2v1", defaultConfig.getProperty( "p2k1" ) );
        assertEquals( "p3v0", defaultConfig.getProperty( "p3k0" ) );
        assertEquals( "p3v1", defaultConfig.getProperty( "p3k1" ) );
        assertNull( defaultConfig.getProperty( "p4k0" ) );
        assertNull( defaultConfig.getProperty( "p4k1" ) );

        Configuration dummyConfig = configuration.getSessionFactoryConfig( "_default" ).getConfiguration();
        assertNull( dummyConfig.getProperty( "p0k0" ) );
        assertNull( dummyConfig.getProperty( "p0k1" ) );
        assertEquals( "p1v0", dummyConfig.getProperty( "p1k0" ) );
        assertEquals( "p1v1", dummyConfig.getProperty( "p1k1" ) );
        assertNull( dummyConfig.getProperty( "p2k0" ) );
        assertNull( dummyConfig.getProperty( "p2k1" ) );
        assertEquals( "p3v0", dummyConfig.getProperty( "p3k0" ) );
        assertEquals( "p3v1", dummyConfig.getProperty( "p3k1" ) );
        assertNull( dummyConfig.getProperty( "p4k0" ) );
        assertNull( dummyConfig.getProperty( "p4k1" ) );
    }
}
