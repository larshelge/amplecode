package no.tfs.nf.api;

import java.util.Collection;

public interface TeamService
    extends GenericService<Team>
{
    Team getByCode( String code );
    
    Collection<Team> getLikeName( String name );
}
