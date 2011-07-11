package no.tfs.nf.service;

import java.util.Collection;

import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonDao;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.util.DefaultGenericService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultPersonService
    extends DefaultGenericService<Person> implements PersonService
{
    @Autowired
    private PersonDao personDao;

    @Override
    public PersonDao getGenericDao()
    {
        return personDao;
    }

    public Person getByCode( String code )
    {
        return personDao.getByCode( code );
    }
    
    @Override
    public Collection<Person> getLikeName( String name )
    {
        return personDao.getLikeName( name );
    }
}
