package org.amplecode.cave.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: PropertiesUtils.java 40 2007-10-28 20:31:42Z torgeilo $
 */
public abstract class PropertiesUtils
{
    public static Properties parseProperties( String value )
    {
        Properties properties = new Properties();

        try
        {
            properties.load( new ByteArrayInputStream( value.getBytes() ) );
        }
        catch ( IOException e )
        {
            // "if an error occurred when reading from the input stream."
        }

        return properties;
    }

    public static Properties add( Properties target, Properties source )
    {
        target.putAll( source );

        return target;
    }
}
