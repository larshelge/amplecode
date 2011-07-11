package no.tfs.nf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
    private static final SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
    
    public static String getDateString( Date date )
    {
        return format.format( date );
    }
    
    public static Date getDate( String string )
    {
        try
        {
            return format.parse( string );
        }
        catch ( ParseException ex )
        {
            throw new RuntimeException( "Failed to parse medium date", ex );
        }
    }

}
