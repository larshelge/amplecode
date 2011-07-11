package no.tfs.nf.web;

import java.util.Collection;
import java.util.Date;

import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Playlist;
import no.tfs.nf.api.PlaylistService;
import no.tfs.nf.api.User;
import no.tfs.nf.api.UserService;
import no.tfs.nf.util.UrlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlaylistController
{
    @Autowired
    private PlaylistService playlistService;
    
    @Autowired
    private ClipService clipService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/playlist")
    public ModelAndView getPlaylist( @RequestParam(value="id",required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "playlist" );
        
        Collection<User> users = userService.getAll();
        users.remove( userService.getCurrentUser() );
        
        if ( id != null )
        {
            Playlist playlist = playlistService.get( id );
            users.removeAll( playlist.getUsers() );
            
            mav.addObject( "playlist", playlist );
            mav.addObject( "users", users );
        }
        else
        {
            mav.addObject( "users", users );
        }
        
        return mav;
    }
    
    @RequestMapping("/savePlaylist")
    public String savePlaylist( @ModelAttribute("playlist") Playlist playlist, BindingResult result, @RequestParam(value="users",required=false) Collection<String> users )
    {
        playlist.setOwner( userService.getCurrentUser() );
        playlist.setLastUpdated( new Date() );
        
        playlist.getUsers().clear();
        
        if ( users != null )
        {   
            for ( String user : users )
            {
                playlist.getUsers().add( userService.get( Integer.valueOf( user ) ) );
            }
        }
        
        if ( playlist.getId() > 0 )
        {
            playlistService.update( playlist );
        }
        else
        {
            playlistService.save( playlist );
        }
        
        return "forward:listPlaylists";
    }
    
    @RequestMapping("/deletePlaylist")
    public String deletePlaylist( @RequestParam Integer id )
    {
        playlistService.delete( playlistService.get( id ) );
        
        return "forward:listPlaylists";
    }

    @RequestMapping("/listPlaylists")
    @Secured({"ROLE_USER"})
    public ModelAndView getPlaylists()
    {
        return new ModelAndView( "playlists" ).addObject( "playlists", playlistService.getOwnedByCurrentUser() );
    }

    @RequestMapping("/myPlaylists")
    public ModelAndView getMyPlaylists( @RequestParam Integer clipId )
    {
        ModelAndView mav = new ModelAndView( "myPlaylists" );
        
        mav.addObject( "playlists", playlistService.getOwnedByCurrentUser( UrlUtils.MAX_PLAYLIST_RESULTS ) );
        mav.addObject( "clip", clipService.get( clipId ) );
        
        return mav;
    }
    
    @RequestMapping("saveMyPlaylist")
    public @ResponseBody Boolean saveMyPlaylist( @RequestParam String name )
    {
        Playlist playlist = new Playlist();
        
        playlist.setName( name );
        playlist.setDescription( name );
        playlist.setOwner( userService.getCurrentUser() );
        playlist.setLastUpdated( new Date() );
        
        return playlistService.save( playlist ) != 0;
    }

    @RequestMapping("/searchMyPlaylists")
    public ModelAndView searchMyPlaylists( @RequestParam String term, @RequestParam Integer clipId )
    {
        ModelAndView mav = new ModelAndView( "myPlaylists" );

        mav.addObject( "playlists", playlistService.getOwnedByCurrentUserLikeName( term, UrlUtils.MAX_PLAYLIST_RESULTS ) );
        mav.addObject( "clip", clipService.get( clipId ) );
        
        return mav;
    }
    
    @RequestMapping("/addToPlaylist")
    public @ResponseBody Boolean addToPlaylist( @RequestParam Integer playlistId, @RequestParam Integer clipId )
    {
        Clip clip = clipService.get( clipId );
        Playlist playlist = playlistService.get( playlistId );
        
        return playlistService.addClipToPlaylist( playlist, clip );
    }
    
    @RequestMapping("/removeFromPlaylist")
    public @ResponseBody Boolean removeFromPlaylist( @RequestParam Integer playlistId, @RequestParam Integer clipId )
    {
        Clip clip = clipService.get( clipId );
        Playlist playlist = playlistService.get( playlistId );
        
        return playlistService.removeClipFromPlaylist( playlist, clip );
    }
}
