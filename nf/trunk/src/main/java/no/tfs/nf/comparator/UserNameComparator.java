package no.tfs.nf.comparator;

import java.util.Comparator;

import no.tfs.nf.api.User;

public class UserNameComparator
    implements Comparator<User>
{
    @Override
    public int compare( User object1, User object2 )
    {
        if ( object1 == null )
        {
            return -1;
        }
        
        if ( object2 == null )
        {
            return 1;
        }
        
        return object1.getUsername().compareTo( object2.getUsername() );
    }
}
