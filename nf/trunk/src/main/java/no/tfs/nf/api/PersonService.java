package no.tfs.nf.api;

import java.util.Collection;

public interface PersonService
    extends GenericService<Person>
{
    Person getByCode( String code );
    
    Collection<Person> getLikeName( String name );
}
