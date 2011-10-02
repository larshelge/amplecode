package no.tfs.nf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
    private static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
    
    public static String getDateString( Date date )
    {
        return SHORT_FORMAT.format( date );
    }
    
    public static Date getDate( String string )
    {
        try
        {
            return SHORT_FORMAT.parse( string );
        }
        catch ( ParseException ex )
        {
            throw new RuntimeException( "Failed to parse medium date", ex );
        }
    }
}
