package no.tfs.nf.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils
{
    private static final List<String> VIDEOFILE_EXTS = Arrays.asList( ".mp4", ".flv" );
    private static final String EXT_SEPARATOR = ".";
    
    private static final Log log = LogFactory.getLog( FileUtils.class );
    
    /**
     * Determines whether the given directory is valid, i.e. whether it is writeable,
     * can be created and can be accessed.
     */
    public static boolean directoryIsValid( File directory ) 
    {
        if ( directory.exists() )
        {
            if( !directory.canWrite() ) 
            {
                log.warn( "Directory " + directory.getAbsolutePath() + " is not writeable" );
                return false;
            }
        }
        else 
        {
            try 
            {
                if ( !directory.mkdirs() )
                {
                    log.warn( "Directory " + directory.getAbsolutePath() + " cannot be created" );
                    return false;
                }
            } 
            catch( SecurityException ex ) 
            {
                log.warn( "Directory " + directory.getAbsolutePath() + " cannot be accessed" );
                return false;                
            }
        }
        
        return true;
    }

    /**
     * Returns the extension of the given file name.
     */
    public static String getExtension( String filename )
    {
        return filename.substring( filename.lastIndexOf( EXT_SEPARATOR ) );
    }
    
    /**
     * Determines whether the given file name refers to a valid video file name,
     * i.e. if the file name is not null and has a valid extension.
     */
    public static boolean isAcceptedVideoFile( String filename )
    {
        return filename != null && VIDEOFILE_EXTS.contains( getExtension( filename ) );
    }
    
    /**
     * Convenience method for throwing IOException.
     */
    public static void throwIOException( boolean condition, String message )
        throws IOException
    {
        if ( condition )
        {
            throw new IOException( message );
        }
    }
    
    /**
     * Deletes the file with the given path name.
     */
    public static boolean deleteFile( String path )
    {
        return new File( path ).delete();
    }
}
