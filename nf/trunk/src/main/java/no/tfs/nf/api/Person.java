package no.tfs.nf.api;

import javax.persistence.Entity;

import no.tfs.nf.svx.ObjectFactory;
import no.tfs.nf.svx.XPerson;

@Entity
public class Person
    extends Tag
{
    public Person()
    {        
    }
    
    public Person( String code, String name )
    {
        this.code = code;
        this.name = name;
    }
    
    public XPerson toX()
    {
        XPerson person = new ObjectFactory().createXPerson();
        person.setCode( code );
        person.setName( name );
        return person;
    }
}
