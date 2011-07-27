package no.tfs.nf.support.deletionhandler;

import javax.sound.sampled.Clip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.Event;
import no.tfs.nf.api.Person;
import no.tfs.nf.api.Playlist;
import no.tfs.nf.api.Team;
import no.tfs.nf.support.deletion.DeletionHandler;

@Service
public class ClipDeletionHandler
    extends DeletionHandler
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Class<?> getTargetClass()
    {
        return Clip.class;
    }
    
    @Override
    public void deleteEvent( Event event )
    {
        final String sql = "update clip set event_id=null where event_id=" + event.getId();
        
        jdbcTemplate.execute( sql );
    }

    @Override
    public void deleteTeam( Team team )
    {
        final String sql = "update clip set team_id=null where team_id=" + team.getId();
        
        jdbcTemplate.execute( sql );
    }

    @Override
    public void deletePlaylist( Playlist playlist )
    {
        final String sql = "delete from clip_playlist where playlists_id=" + playlist.getId();
        
        jdbcTemplate.execute( sql );
    }

    @Override
    public void deletePerson( Person person )
    {
        final String sql = "delete from clip_person where persons_id=" + person.getId();
        
        jdbcTemplate.execute( sql );        
    }

    @Override
    public void deleteCategory( Category category )
    {
        final String sql = "delete from clipcategory where category_id=" + category.getId();
        
        jdbcTemplate.execute( sql );
    }
}
