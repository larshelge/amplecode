package no.tfs.nf.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TextUtils
{
    private static final String SEP_CLASS = "[., ]";    
    private static final String EMPTY = "";

    public static String join( String[] array, String separator, String encloser )
    {
        List<String> collection = Arrays.asList( array );
        
        return join( collection, separator, encloser );
    }
    
    public static String join( Collection<String> collection, String separator, String encloser )
    {
        if ( collection == null || collection.size() == 0 )
        {
            return EMPTY;
        }
        
        separator = ( separator == null ) ? EMPTY : separator;
        
        StringBuilder string = new StringBuilder();
        
        for ( String s : collection )
        {
            if ( encloser != null )
            {
                string.append( encloser ).append( s ).append( encloser ).append( separator );
            }
            else
            {
                string.append( s ).append( separator );
            }
        }
        
        return string.substring( 0, string.length() - separator.length() );        
    }
    
    public static String getSimilarToWordClause( String field, String value )
    {
        String sql = 
            " lower(" + field + ") similar to '%" + SEP_CLASS + value + SEP_CLASS + "%'" +
            " or lower(" + field + ") similar to '" + value + SEP_CLASS + "%'" +
            " or lower(" + field + ") similar to '%" + SEP_CLASS + value + "'" +
            " or lower(" + field + ") = '" + value + "'";
        
        return sql;
    }
}
