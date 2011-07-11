package no.tfs.nf.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.tfs.nf.api.GenericDao;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamDao;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.util.DefaultGenericService;

@Service
@Transactional
public class DefaultTeamService
    extends DefaultGenericService<Team> implements TeamService
{
    @Autowired
    private TeamDao teamDao;
    
    @Override
    public GenericDao<Team> getGenericDao()
    {
        return teamDao;
    }
    
    @Override
    public Team getByCode( String code )
    {
        return teamDao.getByCode( code );
    }
    
    @Override
    public Collection<Team> getLikeName( String name )
    {
        return teamDao.getLikeName( name );
    }
}
