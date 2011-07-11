package no.tfs.nf.api;

import java.util.Collection;
import java.util.List;

public interface PlaylistDao
    extends GenericDao<Playlist>
{
    Collection<Playlist> getLikeName( String name );
    
    Collection<Playlist> getByOwner( User owner );
    
    List<Playlist> getByOwner( User owner, int maxResults );
    
    List<Playlist> getByOwnerLikeName( User owner, String name, int maxResults );
    
    Collection<Playlist> getSharedWith( User user );
}
