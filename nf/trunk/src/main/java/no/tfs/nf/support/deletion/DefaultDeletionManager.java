package no.tfs.nf.support.deletion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultDeletionManager
    implements DeletionManager
{
    private static final Log log = LogFactory.getLog( DefaultDeletionManager.class );
    
    private static final String DELETE_METHOD_PREFIX = "delete";
    private static final String ALLOW_METHOD_PREFIX = "allowDelete";
    
    private List<DeletionHandler> handlers = new ArrayList<DeletionHandler>();

    public void setHandlers( List<DeletionHandler> handlers )
    {
        this.handlers = handlers;
    }
    
    // -------------------------------------------------------------------------
    // DeletionManager implementation
    // -------------------------------------------------------------------------
    
    public void execute( Object object )
    {
        Class<?> clazz = object.getClass();

        String className = clazz.getSimpleName();

        // ---------------------------------------------------------------------
        // Verify that object is allowed to be deleted
        // ---------------------------------------------------------------------

        String allowMethodName = ALLOW_METHOD_PREFIX + className;

        String currentHandler = null;

        try
        {
            Method allowMethod = DeletionHandler.class.getMethod( allowMethodName, new Class[] { clazz }  );

            for ( DeletionHandler handler : handlers )
            {   
                currentHandler = handler.getClass().getSimpleName();

                boolean allow = (Boolean) allowMethod.invoke( handler, object );
                
                if ( !allow )
                {
                    throw new DeleteNotAllowedException( DeleteNotAllowedException.ERROR_ASSOCIATED_BY_OTHER_OBJECTS, handler.getClassName() );
                }
            }
        }
        catch ( NoSuchMethodException ex )
        {
            log.error( "Method '" + allowMethodName + "' does not exist on class '" + clazz + "'", ex );
        }
        catch ( IllegalAccessException ex )
        {
            log.error( "Method '" + allowMethodName + "' could not be invoked on DeletionHandler '" + currentHandler + "'", ex );
        }
        catch ( InvocationTargetException ex )
        {
            log.error( "Method '" + allowMethodName + "' threw exception on DeletionHandler '" + currentHandler + "'", ex );
        }

        // ---------------------------------------------------------------------
        // Delete associated objects
        // ---------------------------------------------------------------------
        
        String deleteMethodName = DELETE_METHOD_PREFIX + className;

        try
        {
            Method deleteMethod = DeletionHandler.class.getMethod( deleteMethodName, new Class[] { clazz } );

            for ( DeletionHandler handler : handlers )
            {
                currentHandler = handler.getClass().getSimpleName();
                
                deleteMethod.invoke( handler, object );
            }
        }
        catch ( Exception ex )
        {
            log.error( "Failed to invoke method " + deleteMethodName + " on DeletionHandler '" + currentHandler + "'", ex );
        }
        
        log.info( "Deleted objects associatied with object of type " + className );
    }
}
