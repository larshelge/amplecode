package no.tfs.nf.comparator;

import java.util.Comparator;

import no.tfs.nf.api.Tag;

public class TagNameComparator<T extends Tag>
    implements Comparator<T>
{
    @Override
    public int compare( Tag object1, Tag object2 )
    {
        if ( object1 == null )
        {
            return -1;
        }
        
        if ( object2 == null )
        {
            return 1;
        }
        
        return object1.getName().compareTo( object2.getName() );
    }
}
