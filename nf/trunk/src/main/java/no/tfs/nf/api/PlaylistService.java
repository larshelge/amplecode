package no.tfs.nf.api;

import java.util.Collection;
import java.util.List;

public interface PlaylistService
    extends GenericService<Playlist>
{
    Collection<Playlist> getLikeName( String name );
    
    Collection<Playlist> getOwnedByCurrentUser();

    List<Playlist> getOwnedByCurrentUser( int maxResults );
    
    List<Playlist> getOwnedByCurrentUserLikeName( String name, int maxResults );
    
    Collection<Playlist> getSharedWithCurrentUser();
    
    boolean addClipToPlaylist( Playlist playlist, Clip clip );
    
    boolean removeClipFromPlaylist( Playlist playlist, Clip clip );
}
