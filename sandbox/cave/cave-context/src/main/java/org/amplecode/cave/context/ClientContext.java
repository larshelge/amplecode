package org.amplecode.cave.context;

/**
 * Interface for a client context.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ClientContext.java 50 2007-12-19 17:42:20Z torgeilo $
 */
public interface ClientContext
{
    /**
     * Sets a value in the context, replacing any existing value with the same
     * key.
     */
    void setAttribute( String key, Object value );

    /**
     * Returns a value in the context, or null if the key is not found.
     */
    Object getAttribute( String key );

    /**
     * Removes a value from the context. Does not complain if the key is not
     * found.
     */
    void removeAttribute( String key );
}
