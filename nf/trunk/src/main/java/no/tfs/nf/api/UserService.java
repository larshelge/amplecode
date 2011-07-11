package no.tfs.nf.api;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService
    extends UserDetailsService, GenericService<User>
{
    String getCurrentUsername();
    
    User getCurrentUser();
    
    User getByUsername( String username );
    
    String encodePassword( String password );
    
    void requestUserRestore( String username );
    
    boolean canRestoreUser( String username, String rawRestoreCode );
    
    boolean restoreUser( String username, String rawRestoreCode, String password );
}
