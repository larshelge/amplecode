package no.tfs.nf.service;

import java.util.Collection;

import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class PersonServiceTest
{
    @Autowired
    private PersonService personService;
    
    @Test
    public void saveGet()
    {
        Person person = new Person( "personA", "Persona" );
        
        int id = personService.save( person );
        
        person = personService.get( id );
        
        assertNotNull( person );
    }
    
    @Test
    public void getAll()
    {        
        personService.save( new Person( "personA", "Persona" ) );        
        personService.save( new Person( "personB", "Personb" ) );        
        personService.save( new Person( "personC", "Personc" ) );
        
        Collection<Person> persons = personService.getAll();
        
        assertNotNull( persons );
        assertEquals( 3, persons.size() );
    }
}
