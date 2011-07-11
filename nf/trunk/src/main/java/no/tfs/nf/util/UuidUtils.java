package no.tfs.nf.util;

import java.util.Random;

public class UuidUtils
{
    public static final String CODE_OWNER = "tfs/";
    public static final String CODE_TEAM = "team/";
    public static final String CODE_CATEGORY = "category/";
    
    private static final int UUID_LENGTH = 11;
    
    private static final char[] chars = { 
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    
    public static final String PREFIX_CLIP = "c";
    public static final String PREFIX_PLAYLIST = "p";
    public static final String PREFIX_EVENT = "e";
    public static final String PREFIX_DOCUMENT = "d";
        
    public static String getUuid()
    {
        Random random = new Random();
        
        StringBuilder builder = new StringBuilder( UUID_LENGTH );
        
        for ( int i = 0; i < UUID_LENGTH; i++ )
        {
            builder.append( chars[ random.nextInt( chars.length ) ] );
        }
        
        return builder.toString();
    }
    
    public static String getClipUuid()
    {
        return PREFIX_CLIP + getUuid();
    }
    
    public static String getPlaylistUuid()
    {
        return PREFIX_PLAYLIST + getUuid();
    }
    
    public static String getEventUuid()
    {
        return PREFIX_EVENT + getUuid();
    }
    
    public static String getDocumentUuid()
    {
        return PREFIX_DOCUMENT + getUuid();
    }
}
