package org.amplecode.cave.context.acegi;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextHolderStrategy;
import org.acegisecurity.context.SecurityContextImpl;
import org.amplecode.cave.context.ClientContext;
import org.amplecode.cave.context.ClientContextHolder;

/**
 * Acegi Security context holder strategy which stores the
 * {@link SecurityContext} in the {@link ClientContext}. If values other than
 * the Acegi Security context is stored in the client context, the Cave
 * {@link org.amplecode.cave.context.web.ClientContextSessionIntegrationFilter}
 * should take Acegi
 * {@link org.acegisecurity.context.HttpSessionContextIntegrationFilter}'s
 * place in order for the complete client context to be integrated with
 * {@code HttpSession}.
 * 
 * @see SecurityContextHolder
 * @see org.amplecode.cave.context.web.ClientContextSessionIntegrationFilter
 * @see org.acegisecurity.context.HttpSessionContextIntegrationFilter
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: AcegiSecurityClientContextStrategy.java 53 2007-12-19 18:57:07Z torgeilo $
 */
public class AcegiSecurityClientContextStrategy
    implements SecurityContextHolderStrategy
{
    public static final String KEY_SECURITY_CONTEXT = "__acegi_security_context";

    // -------------------------------------------------------------------------
    // Set as strategy
    // -------------------------------------------------------------------------

    /**
     * Convenience method for setting this class as the Acegi Security context
     * holder strategy. From the Acegi Security documentation for
     * {@link SecurityContextHolder#setStrategyName(String)}: <strong>Do NOT
     * call this method more than once for a given JVM, as it will reinitialize
     * the strategy and adversely affect any existing threads using the old
     * strategy.</strong> Acegi Security, up to and including version 1.0.5,
     * does not let you know which strategy is the active one.
     * 
     * @see SecurityContextHolder#setStrategyName(String)
     */
    public static void setAsSecurityContextHolderStrategy()
    {
        SecurityContextHolder.setStrategyName( AcegiSecurityClientContextStrategy.class.getName() );
    }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private static Class<? extends SecurityContext> securityContextClass = SecurityContextImpl.class;

    /**
     * Sets the {@link SecurityContext} class to instantiate if there is no
     * {@code SecurityContext}.
     */
    public static void setSecurityContextClass( Class<? extends SecurityContext> clazz )
    {
        securityContextClass = clazz;
    }

    // -------------------------------------------------------------------------
    // SecurityContextHolderStrategy
    // -------------------------------------------------------------------------

    /**
     * Removes the Acegi {@link SecurityContext} from the {@link ClientContext}.
     */
    public final void clearContext()
    {
        ClientContext clientContext = ClientContextHolder.getClientContext( false );

        if ( clientContext != null )
        {
            clientContext.removeAttribute( KEY_SECURITY_CONTEXT );
        }
    }

    /**
     * Returns the {@link SecurityContext}. A new instance is created if none
     * exists based on {@link #setSecurityContextClass(Class)}. The default
     * class is Acegi's default {@link SecurityContextImpl}.
     * 
     * @return a {@link SecurityContext} instance, never {@code null} (in
     *         accordance with
     *         {@link SecurityContextHolderStrategy#getContext()}).
     */
    public final SecurityContext getContext()
    {
        ClientContext clientContext = ClientContextHolder.getClientContext();

        SecurityContext securityContext = (SecurityContext) clientContext.getAttribute( KEY_SECURITY_CONTEXT );

        if ( securityContext == null )
        {
            securityContext = (SecurityContext) ClientContextHolder.newInstance( securityContextClass );

            clientContext.setAttribute( KEY_SECURITY_CONTEXT, securityContext );
        }

        return securityContext;
    }

    /**
     * Sets the {@link SecurityContext}.
     */
    public final void setContext( SecurityContext securityContext )
    {
        ClientContext clientContext = ClientContextHolder.getClientContext();

        clientContext.setAttribute( KEY_SECURITY_CONTEXT, securityContext );
    }
}
