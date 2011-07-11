package org.amplecode.cave.context;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ClientContextHolderStrategy.java 34 2007-10-04 10:09:25Z torgeilo $
 */
public interface ClientContextHolderStrategy
{
    /**
     * Sets the ClientContext.
     */
    void setClientContext( ClientContext clientContext );

    /**
     * Returns the ClientContext. Returns null if there is no ClientContext.
     */
    ClientContext getClientContext();

    /**
     * Removes the ClientContext.
     */
    void removeClientContext();
}
