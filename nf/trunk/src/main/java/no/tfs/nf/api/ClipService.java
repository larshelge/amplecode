package no.tfs.nf.api;

import java.util.List;

public interface ClipService
    extends GenericService<Clip>
{
    Clip getByCode( String code );
    
    List<Clip> get( String category, String eventTeam, String person, String playlist );
    
    List<Clip> get( String q, Type type );
    
    List<Clip> getHierarchy( Category category );
    
    Clip getLatest();
    
    List<Clip> getLatest( int number );
    
    List<Clip> getByPlaylist( String code );
    
    boolean bumpViews( int id );
}
