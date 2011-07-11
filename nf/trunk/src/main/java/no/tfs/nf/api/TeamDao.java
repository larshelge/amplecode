package no.tfs.nf.api;

import java.util.Collection;

public interface TeamDao
    extends GenericDao<Team>
{
    Team getByCode( String code );
    
    Collection<Team> getLikeName( String name );
}
