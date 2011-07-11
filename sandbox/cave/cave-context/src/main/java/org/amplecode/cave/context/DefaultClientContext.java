package org.amplecode.cave.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple {@link ClientContext} implementation using a {@link Map}. Not
 * thread-safe.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultClientContext.java 51 2007-12-19 18:12:01Z torgeilo $
 */
public class DefaultClientContext
    implements ClientContext
{
    private static final int INITIAL_CAPACITY = 4;

    private Map<String, Object> attributes;

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContext#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute( String key, Object value )
    {
        if ( attributes == null )
        {
            attributes = new HashMap<String, Object>( INITIAL_CAPACITY );
        }

        attributes.put( key, value );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContext#getAttribute(java.lang.String)
     */
    public Object getAttribute( String key )
    {
        if ( attributes == null )
        {
            return null;
        }

        return attributes.get( key );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute( String key )
    {
        if ( attributes != null )
        {
            attributes.remove( key );

            if ( attributes.isEmpty() )
            {
                // Save space, remove data structure.
                attributes = null;
            }
        }
    }
}
