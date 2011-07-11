package no.tfs.nf.dao;

import java.util.Collection;

import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventDao;
import no.tfs.nf.util.HibernateGenericDao;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateEventDao
    extends HibernateGenericDao<Event> implements EventDao
{
    @Override
    protected Class<Event> getClazz()
    {
        return Event.class;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Event> getLikeName( String name )
    {
        return getCriteria( Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase() ).list();
    }
}
