package no.tfs.nf.support.deletionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.Team;
import no.tfs.nf.support.deletion.DeletionHandler;

@Service
public class EventDeletionHandler
    extends DeletionHandler
{
    @Autowired
    private EventService eventService;
    
    public Class<?> getTargetClass()
    {
        return Event.class;
    }

    @Override
    public void deleteTeam( Team team )
    {
        for ( Event event : eventService.getAll() )
        {
            if ( event.getHomeTeam() != null && event.getHomeTeam().equals( team ) )
            {
                event.setHomeTeam( null );
            }
            
            if ( event.getAwayTeam() != null && event.getAwayTeam().equals( team ) )
            {
                event.setAwayTeam( null );
            }
        }
    }
}
