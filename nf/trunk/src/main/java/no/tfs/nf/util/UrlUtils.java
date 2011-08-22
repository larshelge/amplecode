package no.tfs.nf.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;

public class UrlUtils
{
    private static final Log log = LogFactory.getLog( UrlUtils.class );
    
    private static final int DEFAULT_PORT = 80;    
    private static final String PORT_DELIMITER = ":";    
    private static final String PATH_DELIMITER = "/";
    private static final String EMPTY = "";
    private static final String PROTOCOL_HTTP = "http://";
    
    private static final String DEFAULT_STATIC_BASE_URL = "http://localhost:81/video/";
    private static final String DEFAULT_STATIC_LOCATION = "/var/www/video/";
    private static final String DEFAULT_UPLOAD_LOCATION = "/home/larshelg/dev/video/";
    
    public static final String CONFIG_LOCATION = "/opt/nf/nf.properties";
    
    public static final String KEY_STATIC_BASE_URL = "static.base.url";
    public static final String KEY_STATIC_LOCATION = "static.location";
    public static final String KEY_UPLOAD_LOCATION = "upload.location";
    
    public static final String PARAM_STATIC_BASE_URL = "staticBaseUrl";
    public static final String PARAM_STATIC_LOCATION = "staticLocation";    
    public static final String PARAM_BASE_URL = "baseUrl";
    public static final String PARAM_AUTHORITIES = "authorities";
    public static final String PARAM_CURRENT_USER = "currentUser";
    public static final String PARAM_SUGGESTIONS = "suggestions";
    public static final int MAX_PLAYLIST_RESULTS = 10;
        
    public static final String DELIMITERS = "[,;]";
    
    private static Properties properties = new Properties();
    
    public static void setProperty( Object key, Object value ) // For testing
    {
        UrlUtils.properties.put( key, value );
    }

    static
    {
        try
        {
            properties.load( new FileSystemResource( CONFIG_LOCATION ).getInputStream() );
            
            log.info( "Loaded nf.properties configuration file" );
            log.info( "Static base URL: " + properties.getProperty( KEY_STATIC_BASE_URL ) );
            log.info( "Static location: " + properties.getProperty( KEY_STATIC_LOCATION ) );
            log.info( "Upload location: " + properties.getProperty( KEY_UPLOAD_LOCATION ) );
        }
        catch ( IOException ex )
        {
            log.info( "Configuration file nf.properties not found" );
        }
    }
    
    public static String baseUrl( HttpServletRequest request )
    {
        String port = request.getServerPort() != DEFAULT_PORT ? PORT_DELIMITER + request.getServerPort() : EMPTY;
        
        return PROTOCOL_HTTP + request.getServerName() + port + request.getContextPath() + PATH_DELIMITER;
    }
    
    public static String staticBaseUrl()
    {
        return properties.getProperty( KEY_STATIC_BASE_URL, DEFAULT_STATIC_BASE_URL );
    }
    
    public static String staticLocation()
    {
        return properties.getProperty( KEY_STATIC_LOCATION, DEFAULT_STATIC_LOCATION );
    }
    
    public static String uploadLocation()
    {
        return properties.getProperty( KEY_UPLOAD_LOCATION, DEFAULT_UPLOAD_LOCATION );
    }
}
