package no.tfs.nf.support.deletionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.tfs.nf.api.Playlist;
import no.tfs.nf.api.PlaylistService;
import no.tfs.nf.api.User;
import no.tfs.nf.support.deletion.DeletionHandler;

@Service
public class PlaylistDeletionHandler
    extends DeletionHandler
{
    @Autowired
    private PlaylistService playlistService;
    
    public Class<?> getTargetClass()
    {
        return Playlist.class;
    }

    @Override
    public void deleteUser( User user )
    {
        for ( Playlist playlist : playlistService.getAll() )
        {
            if ( playlist.getUsers().remove( user ) )
            {
                playlistService.update( playlist );
            }
        }
    }
}
