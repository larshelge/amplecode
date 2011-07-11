package org.amplecode.cave.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ResourceUtils.java 40 2007-10-28 20:31:42Z torgeilo $
 */
public abstract class ResourceUtils
{
    /**
     * The prefix of classpath resource paths. The value is {@value #CLASSPATH}.
     */
    public static final String CLASSPATH = "classpath:";

    public static InputStream openInputStream( final String path )
        throws IOException
    {
        String realPath = path;

        Matcher matcher = Pattern.compile( "\\$\\{(.+?)\\}" ).matcher( realPath );

        if ( matcher.matches() )
        {
            StringBuffer buffer = new StringBuffer();

            do
            {
                String variable = matcher.group( 1 );

                String value = System.getenv( variable );

                if ( value == null )
                {
                    throw new RuntimeException( "There is no environment variable named \"" + variable + "\"" );
                }

                matcher.appendReplacement( buffer, value );
            }
            while ( matcher.find() );

            matcher.appendTail( buffer );

            realPath = buffer.toString();
        }

        if ( realPath.startsWith( CLASSPATH ) )
        {
            realPath = realPath.substring( CLASSPATH.length() );

            return getResourceAsStream( realPath );
        }
        else
        {
            return new FileInputStream( realPath );
        }
    }

    public static InputStream getResourceAsStream( final String path )
    {
        URL url = getResource( path );

        if ( url == null )
        {
            return null;
        }

        try
        {
            return url.openStream();
        }
        catch ( IOException e )
        {
            return null;
        }
    }

    public static URL getResource( final String path )
    {
        URL url = ResourceUtils.class.getResource( path );

        if ( url == null )
        {
            url = Thread.currentThread().getContextClassLoader().getResource( path );
        }

        return url;
    }
}
