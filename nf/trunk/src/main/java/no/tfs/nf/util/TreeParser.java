package no.tfs.nf.util;

import no.tfs.nf.api.TreeNode;

public class TreeParser
{
    private static final String ITEM_END = "</li>";
    private static final String LIST_START = "<ul>";
    private static final String LIST_END = "</ul>";
            
    public static String parse( TreeNode node )
    {
        StringBuffer out = new StringBuffer();
        
        out.append( LIST_START );
        
        parseRecursive( node, out );
        
        out.append( LIST_END );
        
        return out.toString();
    }
    
    private static void parseRecursive( TreeNode node, StringBuffer out )
    {
        out.append( "<li id=\"cat" + node.getId() + "\">" ).append( "<a href=\"javascript:searchHierarchy( '" + node.getId() + "' )\">" + node.getName() + "</a>" );
        
        if ( !node.hasChildren() )
        {
            out.append( ITEM_END );
            
            return;
        }

        out.append( LIST_START );
        
        for ( TreeNode child : node.getChildren() )
        {
            parseRecursive( child, out );
        }        

        out.append( LIST_END ).append( ITEM_END );
    }
}
