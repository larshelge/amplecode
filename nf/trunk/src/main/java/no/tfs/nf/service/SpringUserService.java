package no.tfs.nf.service;

import java.util.UUID;

import no.tfs.nf.api.User;
import no.tfs.nf.api.UserDao;
import no.tfs.nf.api.UserService;
import no.tfs.nf.util.DefaultGenericService;
import no.tfs.nf.util.EmailUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class SpringUserService
    extends DefaultGenericService<User> implements UserService
{
    private static final Log log = LogFactory.getLog( SpringUserService.class );
    
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private SystemWideSaltSource saltSource;

    @Override
    public UserDao getGenericDao()
    {
        return userDao;
    }
    
    @Override
    public UserDetails loadUserByUsername( String username )
        throws UsernameNotFoundException, DataAccessException
    {
        User user = userDao.getByUsername( username );
        
        return user != null ? user.getUserDetails() : null;
    }

    @Override
    public String getCurrentUsername()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null )
        {
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if ( principal instanceof org.springframework.security.core.userdetails.User )
        {
            return ((org.springframework.security.core.userdetails.User) principal).getUsername();
        }
                
        return (String) principal;
    }

    @Override
    public User getCurrentUser()
    {
        return userDao.getByUsername( getCurrentUsername() );
    }
    
    @Override
    public User getByUsername( String username )
    {
        return userDao.getByUsername( username );
    }

    @Override
    public String encodePassword( String password )
    {
        return passwordEncoder.encodePassword( password, saltSource.getSystemWideSalt() );
    }
    
    @Override
    public void requestUserRestore( String username )
    {
        User user = getByUsername( username );
        
        String rawRestoreCode = UUID.randomUUID().toString();
        
        String hashedRestoreCode = passwordEncoder.encodePassword( rawRestoreCode, saltSource.getSystemWideSalt() );
        
        user.setRestoreCode( hashedRestoreCode );
        
        update( user );
        
        EmailUtils.sendResetPasswordEmail( user, rawRestoreCode );
        
        log.info( "Requested user restore email for: " + username );
    }

    @Override
    public boolean canRestoreUser( String username, String rawRestoreCode )
    {
        User user = getByUsername( username );

        String hashedRestoreCode = passwordEncoder.encodePassword( rawRestoreCode, saltSource.getSystemWideSalt() );
        
        boolean canRestore = user != null && hashedRestoreCode != null && hashedRestoreCode.equals( user.getRestoreCode() );
        
        log.info( "Requested user restore check for: " + username + " outcome: " + canRestore );
        
        return canRestore;
    }

    @Override
    public boolean restoreUser( String username, String rawRestoreCode, String password )
    {
        if ( !canRestoreUser( username, rawRestoreCode ) )
        {
            return false;
        }        

        String encodedPassword = passwordEncoder.encodePassword( password, saltSource.getSystemWideSalt() );
        
        User user = getByUsername( username );
        
        user.setPassword( encodedPassword );
        user.setRestoreCode( null );
        
        update( user );
        
        log.info( "Restored user successfully: " + user );
        
        return true;
    }
}
