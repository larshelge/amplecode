package no.tfs.nf.api;

import java.util.List;

public interface ClipDao
    extends GenericDao<Clip>
{
    List<Clip> get( String category, String eventTeam, String person, String playlist );
    
    List<Clip> get( String q, Type type );
    
    List<Clip> getHierarchy( Category category );
    
    Clip getByCode( String code );
    
    Clip getFirstInPlaylist( String code );
    
    Clip getLatest();
    
    List<Clip> getLatest( int number );
}
