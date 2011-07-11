package org.amplecode.cave.context;

/**
 * Simle {@link ThreadLocal} implementation of
 * {@link ClientContextHolderStrategy}.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ThreadLocalHolderStrategy.java 50 2007-12-19 17:42:20Z torgeilo $
 */
public class ThreadLocalHolderStrategy
    implements ClientContextHolderStrategy
{
    private ThreadLocal<ClientContext> holder = new ThreadLocal<ClientContext>();

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContextHolderStrategy#setClientContext(org.amplecode.cave.context.ClientContext)
     */
    public void setClientContext( ClientContext clientContext )
    {
        holder.set( clientContext );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContextHolderStrategy#getClientContext()
     */
    public ClientContext getClientContext()
    {
        return holder.get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.amplecode.cave.context.ClientContextHolderStrategy#removeClientContext()
     */
    public void removeClientContext()
    {
        holder.remove();
    }
}
