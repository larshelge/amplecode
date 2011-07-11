package no.tfs.nf.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Collection;

import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class PersonDaoTest
{
    @Autowired
    private PersonDao personDao;
    
    @Test
    public void saveGet()
    {
        Person person = new Person( "PersonA", "persona" );
        
        int id = personDao.save( person );
        
        assertNotNull( personDao.get( id ) );
    }
    
    @Test
    public void getAll()
    {        
        personDao.save( new Person( "persona", "PersonA" ) );        
        personDao.save( new Person( "personb", "PersonB" ) );        
        personDao.save( new Person( "personc", "PersonC" ) );
        
        Collection<Person> persons = personDao.getAll();
        
        assertNotNull( persons );
        assertEquals( 3, persons.size() );
    }
}
