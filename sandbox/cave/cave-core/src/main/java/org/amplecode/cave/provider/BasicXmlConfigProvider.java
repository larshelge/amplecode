package org.amplecode.cave.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;

import org.amplecode.cave.CaveConfiguration;
import org.amplecode.cave.SessionFactoryConfig;
import org.amplecode.cave.util.PropertiesUtils;
import org.amplecode.cave.util.ResourceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Default implementation of ConfigProvider. Can be used to set up the
 * configuration stream and create instances of this class. Supports
 * modifications of configurations after parsing.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: BasicXmlConfigProvider.java 45 2007-12-19 15:36:52Z torgeilo $
 */
public class BasicXmlConfigProvider
    extends DefaultHandler
    implements ConfigProvider
{
    private final Log log = LogFactory.getLog( BasicXmlConfigProvider.class );

    private static final String INCLUDABLE = "includable";

    private static final String SESSION_FACTORY = "sessionFactory";

    private static final String PROPERTIES = "properties";

    private static final String MAPPING_RESOURCES = "mappingResources";

    private static final String RESOURCE = "resource";

    private static final String INCLUDE = "include";

    private static final String DEFAULT_SESSION_FACTORY = "defaultSessionFactory";

    private static final String TEST_SESSION_FACTORY = "testSessionFactory";

    private static final String ID = "id";

    private static final String NAME = "name";

    private static final String DESCRIPTION = "description";

    private static final String SOURCE = "src";

    private static final String ID_REF = "ref";

    /**
     * The XML schema used for validating the configuration. The value is
     * {@value #SCHEMA}.
     */
    public static final String SCHEMA = "cave-basic.xsd";

    private Stack<Object> objectStack = new Stack<Object>();

    private Map<String, Includable> includables = new HashMap<String, Includable>();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CaveConfiguration configuration;

    public void setConfiguration( CaveConfiguration configuration )
    {
        this.configuration = configuration;
    }

    private String configPath;

    public void setConfigPath( String configPath )
    {
        this.configPath = configPath;
    }

    // -------------------------------------------------------------------------
    // ConfigurationParser implementation
    // -------------------------------------------------------------------------

    public final void provide()
        throws Exception
    {
        InputStream inputStream;

        try
        {
            inputStream = ResourceUtils.openInputStream( configPath );
        }
        catch ( IOException e )
        {
            log.warn( "Unable to load path: \"" + configPath + "\"" );

            return;
        }

        try
        {
            parse( inputStream );
        }
        catch ( Exception e )
        {
            objectStack.clear();

            throw e;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( IOException e )
            {
                log.error( "Failed to close input stream" );
            }

            includables.clear();
        }

        if ( !objectStack.empty() )
        {
            log.error( "Internal object stack not empty, but parse completed successfully [PEEK]" + objectStack.peek()
                + "[END]" );
        }
    }

    private void parse( final InputStream configStream )
        throws Exception
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );
        URL schemaURL = ResourceUtils.getResource( SCHEMA );

        if ( schemaURL == null )
        {
            throw new FileNotFoundException( "Unable to find schema on classpath: " + SCHEMA );
        }

        Schema schema = schemaFactory.newSchema( schemaURL );

        ValidatorHandler validatorHandler = schema.newValidatorHandler();
        validatorHandler.setContentHandler( this );

        /*
         * We don't need an error handler. If the XML document has errors, the
         * validator will fail hard with a SAXException.
         */

        InputSource source = new InputSource( configStream );

        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler( validatorHandler );
        reader.parse( source );
    }

    // -------------------------------------------------------------------------
    // SAX callbacks
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @SuppressWarnings( "unchecked" )
    public final void startElement( String namespace, String localName, String qualifiedName, Attributes attributes )
        throws SAXException
    {
        if ( INCLUDABLE.equals( localName ) )
        {
            String id = attributes.getValue( ID );

            Includable includable = new Includable();

            includables.put( id, includable );

            objectStack.push( includable );
        }
        else if ( PROPERTIES.equals( localName ) )
        {
            objectStack.push( new StringBuffer() );
        }
        else if ( MAPPING_RESOURCES.equals( localName ) )
        {
            objectStack.push( new HashSet<String>() );
        }
        else if ( RESOURCE.equals( localName ) )
        {
            String resource = attributes.getValue( SOURCE );

            Set<String> mappingResources = (Set<String>) objectStack.peek();

            mappingResources.add( resource );
        }
        else if ( SESSION_FACTORY.equals( localName ) )
        {
            String id = attributes.getValue( ID );

            SessionFactoryConfig config = configuration.getOrCreateSessionFactoryConfig( id );

            objectStack.push( config );
        }
        else if ( NAME.equals( localName ) )
        {
            objectStack.push( new StringBuffer() );
        }
        else if ( DESCRIPTION.equals( localName ) )
        {
            objectStack.push( new StringBuffer() );
        }
        else if ( INCLUDE.equals( localName ) )
        {
            String ref = attributes.getValue( ID_REF );

            if ( !includables.containsKey( ref ) )
            {
                throw new SAXException( "Element include doens't reference an includable id" );
            }

            Includable includable = includables.get( ref );
            SessionFactoryConfig config = (SessionFactoryConfig) objectStack.peek();

            Configuration configuration = config.getConfiguration();

            configuration.addProperties( includable.getHibernateProperties() );

            for ( String resource : includable.getMappingResources() )
            {
                configuration.addResource( resource );
            }
        }
        else if ( DEFAULT_SESSION_FACTORY.equals( localName ) )
        {
            String ref = attributes.getValue( ID_REF );

            if ( configuration.getSessionFactoryConfig( ref ) == null )
            {
                throw new SAXException( "Element defaultDatabase doesn't reference a database id" );
            }

            configuration.setDefaultConfigId( ref );
        }
        else if ( TEST_SESSION_FACTORY.equals( localName ) )
        {
            String ref = attributes.getValue( ID_REF );

            if ( configuration.getSessionFactoryConfig( ref ) == null )
            {
                throw new SAXException( "Element testDatabase doesn't reference a database id" );
            }

            configuration.setTestConfigId( ref );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings( "unchecked" )
    public final void endElement( String namespace, String localName, String qualifiedName )
        throws SAXException
    {
        if ( INCLUDABLE.equals( localName ) )
        {
            objectStack.pop();
        }
        else if ( PROPERTIES.equals( localName ) )
        {
            String value = objectStack.pop().toString();

            if ( value.trim().length() == 0 )
            {
                throw new SAXException( "Element hibernateProperties cannot be empty" );
            }

            Properties properties = PropertiesUtils.parseProperties( value );

            Object parent = objectStack.peek();

            if ( parent instanceof Includable )
            {
                ((Includable) parent).getHibernateProperties().putAll( properties );
            }
            else
            {
                ((SessionFactoryConfig) parent).getConfiguration().addProperties( properties );
            }
        }
        else if ( MAPPING_RESOURCES.equals( localName ) )
        {
            Set<String> mappingResources = (Set<String>) objectStack.pop();

            Object parent = objectStack.peek();

            if ( parent instanceof Includable )
            {
                ((Includable) parent).getMappingResources().addAll( mappingResources );
            }
            else
            {
                Configuration config = ((SessionFactoryConfig) parent).getConfiguration();

                for ( String resource : mappingResources )
                {
                    config.addResource( resource );
                }
            }
        }
        else if ( SESSION_FACTORY.equals( localName ) )
        {
            SessionFactoryConfig config = (SessionFactoryConfig) objectStack.pop();

            if ( config.getConfiguration().getProperties().isEmpty() )
            {
                throw new SAXException( "Element database [" + config.getId()
                    + "] doesn't have any declared or included Hibernate properties" );
            }
        }
        else if ( NAME.equals( localName ) )
        {
            String value = objectStack.pop().toString();

            if ( value.trim().length() == 0 )
            {
                throw new SAXException( "Element name cannot be empty" );
            }

            SessionFactoryConfig config = (SessionFactoryConfig) objectStack.peek();

            config.setName( value );
        }
        else if ( DESCRIPTION.equals( localName ) )
        {
            String value = objectStack.pop().toString();

            if ( value.trim().length() == 0 )
            {
                throw new SAXException( "Element description cannot be empty" );
            }

            SessionFactoryConfig config = (SessionFactoryConfig) objectStack.peek();

            config.setDescription( value );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public final void characters( char[] chars, int offset, int length )
        throws SAXException
    {
        StringBuffer buffer = (StringBuffer) objectStack.peek();

        buffer.append( chars, offset, length );
    }

    // -------------------------------------------------------------------------
    // Includable
    // -------------------------------------------------------------------------

    private class Includable
    {
        private final Properties hibernateProperties = new Properties();

        private final Set<String> mappingResources = new HashSet<String>();

        // ---------------------------------------------------------------------
        // Getters
        // ---------------------------------------------------------------------

        public final Properties getHibernateProperties()
        {
            return hibernateProperties;
        }

        public final Set<String> getMappingResources()
        {
            return mappingResources;
        }
    }
}
