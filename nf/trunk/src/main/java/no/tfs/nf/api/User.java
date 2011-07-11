package no.tfs.nf.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name="usertable")
public class User
{
    public static final String[] USERROLES = { "ROLE_USER", "ROLE_TRUSTED_USER", "ROLE_MANAGER", "ROLE_ADMIN" };
    
    public static Map<String, List<String>> AUTHORITIES;
    
    static
    {
        AUTHORITIES = new HashMap<String, List<String>>();        
        AUTHORITIES.put( USERROLES[0], Arrays.asList( USERROLES[0] ) );
        AUTHORITIES.put( USERROLES[1], Arrays.asList( USERROLES[0], USERROLES[1] ) );
        AUTHORITIES.put( USERROLES[2], Arrays.asList( USERROLES[0], USERROLES[1], USERROLES[2] ) );
        AUTHORITIES.put( USERROLES[3], Arrays.asList( USERROLES[0], USERROLES[1], USERROLES[2], USERROLES[3] ) );
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Column(unique=true,nullable=false)
    private String username;
    
    @Column(nullable=false)
    private String password;
    
    private String firstname;
    
    private String lastname;
    
    @Column(nullable=false)
    private String userrole;
    
    private String restoreCode;
    
    public User()
    {
    }
    
    public User( String username, String password, String firstname, String lastname, String userrole )
    {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userrole = userrole;
    }
    
    /**
     * User roles and authorities are currently the same. Returns the given authority
     * and lower authorities.
     */
    public Collection<GrantedAuthority> getGrantedAuthorities()
    {
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        
        for ( String authority : AUTHORITIES.get( userrole ) )
        {
            authorities.add( new GrantedAuthorityImpl( authority ) );
        }
        
        return authorities;
    }
        
    public boolean hasAuth( String role )
    {
        return AUTHORITIES.get( userrole ) != null && AUTHORITIES.get( userrole ).contains( role );
    }
    
    public Set<String> getAuthorities()
    {
        return new HashSet<String>( AUTHORITIES.get( userrole ) );
    }
    
    public UserDetails getUserDetails()
    {        
        return new org.springframework.security.core.userdetails.User( 
            username, password, true, true, true, true, getGrantedAuthorities() );
    }
    
    public String getName()
    {
        return firstname + " " + lastname;
    }
    
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname( String firstname )
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname( String lastname )
    {
        this.lastname = lastname;
    }

    public String getUserrole()
    {
        return userrole;
    }

    public void setUserrole( String userrole )
    {
        this.userrole = userrole;
    }

    public String getRestoreCode()
    {
        return restoreCode;
    }

    public void setRestoreCode( String restoreCode )
    {
        this.restoreCode = restoreCode;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( username == null ) ? 0 : username.hashCode() );
        
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        User other = (User) object;
        
        return username.equals( other.username );
    }
    
    @Override
    public String toString()
    {
        return "[" + username + "]";
    }
}
