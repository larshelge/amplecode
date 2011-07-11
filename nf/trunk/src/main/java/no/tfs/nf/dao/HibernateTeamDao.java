package no.tfs.nf.dao;

import java.util.Collection;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamDao;
import no.tfs.nf.util.HibernateGenericDao;

@Repository
public class HibernateTeamDao
    extends HibernateGenericDao<Team> implements TeamDao
{
    @Override
    protected Class<Team> getClazz()
    {
        return Team.class;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Team> getLikeName( String name )
    {
        return getCriteria( Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase() ).list();
    }
}
