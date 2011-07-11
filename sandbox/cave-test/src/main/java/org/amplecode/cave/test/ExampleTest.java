package org.amplecode.cave.test;

import static org.junit.Assert.assertNotNull;

import java.util.Random;
import java.util.Map.Entry;

import org.amplecode.cave.CaveClientManager;
import org.amplecode.cave.CaveConfiguration;
import org.amplecode.cave.SessionFactoryConfig;
import org.amplecode.cave.provider.BasicXmlConfigProvider;
import org.amplecode.cave.proxy.SessionFactoryProxyFactory;
import org.amplecode.cave.session.SessionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ExampleTest.java 45 2007-12-19 15:36:52Z torgeilo $
 */
public class ExampleTest
{
    public static void main( String[] args )
        throws Exception
    {
        CaveConfiguration configuration = new CaveConfiguration();
        BasicXmlConfigProvider provider = new BasicXmlConfigProvider();
        provider.setConfiguration( configuration );
        provider.setConfigPath( "classpath:/example.xml" );
        provider.provide();

        System.out.println( "-----------------------------------" );
        System.out.println( "Default database: " + configuration.getDefaultConfigId() );
        System.out.println( "Test database: " + configuration.getTestConfigId() );

        for ( SessionFactoryConfig database : configuration.getSessionFactoryConfigs() )
        {
            System.out.println( "Database" );
            System.out.println( "  ID: " + database.getId() );
            System.out.println( "  Name: " + database.getName() );
            System.out.println( "  Description: " + database.getDescription() );
            System.out.println( "  Properties:" );

            for ( Entry<Object, Object> entry : database.getConfiguration().getProperties().entrySet() )
            {
                System.out.println( "    " + entry.getKey().toString() + " = " + entry.getValue().toString() );
            }
        }
        System.out.println( "-----------------------------------" );

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------

        SessionFactoryConfig testConfig = configuration.getSessionFactoryConfig( configuration.getTestConfigId() );

        SessionFactory sessionFactory = testConfig.buildSessionFactory();

        System.out.println( "A" );

        Session session = sessionFactory.getCurrentSession();

        Person person = new Person( "Per", "per@norge.no" );
        int id = 0;

        Transaction t = session.beginTransaction();
        try
        {
            id = (Integer) session.save( person );
            t.commit();
        }
        catch ( HibernateException e )
        {
            t.rollback();
            SessionUtils.closeSession( session );
        }

        System.out.println( "C" );

        SessionUtils.closeSession( session );

        System.out.println( "D" );

        session = sessionFactory.getCurrentSession();
        person = (Person) session.get( Person.class, id );
        assertNotNull( person );

        System.out.println( "E" );

        session.close();

        System.out.println( "F" );

        System.out.println( "G" );

        System.out.println( "-----------------------------------" );

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------

        int numThreads = 25;
        int numNames = 1000;

        for ( int i = 0; i < numThreads; ++i )
        {
            Client client = new Client( testConfig, numNames / numThreads );

            new Thread( client ).start();
        }
    }

    private static class Client
        implements Runnable
    {
        private static final Log log = LogFactory.getLog( Client.class );

        private static char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

        private SessionFactoryConfig sessionFactoryConfig;

        private SessionFactory sessionFactory;

        private int numNames;

        private Random random = new Random();

        public Client( SessionFactoryConfig sessionFactoryConfig, int numNames )
        {
            this.numNames = numNames;
            this.sessionFactoryConfig = sessionFactoryConfig;
        }

        private String createName()
        {
            StringBuffer buffer = new StringBuffer();

            for ( int i = 0; i < 10; ++i )
            {
                buffer.append( alphabet[random.nextInt( alphabet.length )] );
            }

            return buffer.toString();
        }

        public void run()
        {
            // -----------------------------------------------------------------
            // Swap session factory
            // -----------------------------------------------------------------

            CaveClientManager.setClientSessionFactoryConfig( sessionFactoryConfig );

            sessionFactoryConfig = null;

            sessionFactory = SessionFactoryProxyFactory.getProxy();

            // -----------------------------------------------------------------

            for ( int i = 0; i < numNames; ++i )
            {
                Session session = sessionFactory.getCurrentSession();

                Person person = new Person( createName(), createName() );

                Transaction t = session.beginTransaction();

                try
                {
                    log.info( "Saving: " + person.getName() );

                    session.save( person );

                    t.commit();
                }
                catch ( HibernateException e )
                {
                    log.error( "Failed to add " + person.getName() );

                    t.rollback();
                }
                finally
                {
                    SessionUtils.closeSession( session );
                }
            }
        }
    }
}
