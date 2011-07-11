package no.tfs.nf.dao;

import java.util.Collection;

import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonDao;
import no.tfs.nf.util.HibernateGenericDao;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class HibernatePersonDao
    extends HibernateGenericDao<Person> implements PersonDao
{
    @Override
    protected Class<Person> getClazz()
    {
        return Person.class;
    }
        
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Person> getLikeName( String name )
    {
        return getCriteria( Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase() ).list();
    }
}
