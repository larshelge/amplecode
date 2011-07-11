package no.tfs.nf.api;

import java.util.Collection;

public interface PersonDao
    extends GenericDao<Person>
{
    Person getByCode( String code );
    
    Collection<Person> getLikeName( String name );
}
