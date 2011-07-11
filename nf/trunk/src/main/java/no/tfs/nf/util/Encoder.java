package no.tfs.nf.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

public class Encoder
{
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
    
    public String html( String value )
    {
        return StringEscapeUtils.escapeHtml( value );
    }
    
    public String xml( String value )
    {
        return StringEscapeUtils.escapeXml( value );
    }
    
    public String js( String value )
    {
        return StringEscapeUtils.escapeJavaScript( value );
    }

    public String date( Date date )
    {
        return date != null ? DATE_FORMAT.format( date ) : null;
    }
}
