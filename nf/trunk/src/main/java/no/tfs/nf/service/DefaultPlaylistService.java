package no.tfs.nf.service;

import java.util.Collection;
import java.util.List;

import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Playlist;
import no.tfs.nf.api.PlaylistDao;
import no.tfs.nf.api.PlaylistService;
import no.tfs.nf.api.User;
import no.tfs.nf.api.UserService;
import no.tfs.nf.util.DefaultGenericService;
import no.tfs.nf.util.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultPlaylistService
    extends DefaultGenericService<Playlist> implements PlaylistService
{
    @Autowired
    private PlaylistDao playlistDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ClipService clipService;
    
    @Override
    public PlaylistDao getGenericDao()
    {
        return playlistDao;
    }

    @Override
    public int save( Playlist playlist )
    {
        playlist.setCode( playlist.getCode() == null || playlist.getCode().trim().isEmpty() ? UuidUtils.getPlaylistUuid() : playlist.getCode() );
        
        return playlistDao.save( playlist );
    }
    
    @Override
    public Collection<Playlist> getLikeName( String name )
    {
        return playlistDao.getLikeName( name );
    }

    @Override
    public Collection<Playlist> getOwnedByCurrentUser()
    {
        User currentUser = userService.getCurrentUser();
        
        return playlistDao.getByOwner( currentUser );
    }

    @Override
    public List<Playlist> getOwnedByCurrentUser( int maxResults )
    {
        User currentUser = userService.getCurrentUser();
        
        return playlistDao.getByOwner( currentUser, maxResults );
    }

    @Override
    public List<Playlist> getOwnedByCurrentUserLikeName( String name, int maxResults )
    {
        User currentUser = userService.getCurrentUser();
        
        return playlistDao.getByOwnerLikeName( currentUser, name, maxResults );
    }
    
    @Override
    public Collection<Playlist> getSharedWithCurrentUser()
    {
        User currentUser = userService.getCurrentUser();
        
        return playlistDao.getSharedWith( currentUser );
    }
    
    @Override
    public boolean addClipToPlaylist( Playlist playlist, Clip clip )
    {
        boolean result = clip.getPlaylists().add( playlist );
        
        if ( result )
        {
            clipService.save( clip );
        }
        
        return result;
    }

    @Override
    public boolean removeClipFromPlaylist( Playlist playlist, Clip clip )
    {
        boolean result = clip.getPlaylists().remove( playlist );
        
        if ( result )
        {
            clipService.save( clip );
        }
        
        return result;
    }
}
