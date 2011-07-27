package no.tfs.nf.support.deletion;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

public class DeletionInterceptor
{
    @Autowired
    private DeletionManager deletionManager;
    
    public void intercept( JoinPoint joinPoint )
    {
        if ( joinPoint.getArgs() != null && joinPoint.getArgs().length > 0 )
        {
            deletionManager.execute( joinPoint.getArgs()[0] );
        }
    }
}
