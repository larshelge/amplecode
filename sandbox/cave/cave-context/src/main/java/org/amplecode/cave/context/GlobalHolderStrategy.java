package org.amplecode.cave.context;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: GlobalHolderStrategy.java 51 2007-12-19 18:12:01Z torgeilo $
 */
public class GlobalHolderStrategy
    implements ClientContextHolderStrategy
{
    private ClientContext clientContext;

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContextHolderStrategy#setClientContext(org.amplecode.cave.context.ClientContext)
     */
    public void setClientContext( ClientContext clientContext )
    {
        this.clientContext = clientContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContextHolderStrategy#getClientContext()
     */
    public ClientContext getClientContext()
    {
        return clientContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContextHolderStrategy#removeClientContext()
     */
    public void removeClientContext()
    {
        clientContext = null;
    }
}
