package no.tfs.nf.web;

import static no.tfs.nf.util.CollectionUtilsWrapper.sort;

import java.util.Date;

import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.comparator.TagNameComparator;
import no.tfs.nf.util.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EventController
{
    @Autowired
    private EventService eventService;
    
    @Autowired
    private TeamService teamService;
    
    @InitBinder
    public void initBinder( WebDataBinder binder )
    {
        binder.registerCustomEditor( Date.class, new CustomDateEditor( Encoder.DATE_FORMAT, false ) );
    }

    @RequestMapping("/event")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView getEvent( @RequestParam(value="id",required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/event" );
        
        if ( id != null )
        {
            mav.addObject( "event", eventService.get( id ) );
        }

        mav.addObject( "teams", sort( teamService.getAll(), new TagNameComparator<Team>() ) );
        
        return mav;
    }

    @RequestMapping("/saveEvent")
    @Secured({"ROLE_MANAGER"})
    public String saveEvent( @ModelAttribute("event") Event event, BindingResult result, @RequestParam Integer homeTeam, @RequestParam Integer awayTeam )
    {
        event.setHomeTeam( teamService.get( homeTeam ) );
        event.setAwayTeam( teamService.get( awayTeam ) );
        
        if ( event.getId() > 0 )
        {
            eventService.update( event );
        }
        else
        {
            eventService.save( event );
        }
        
        return "forward:listEvents";
    }
    
    @RequestMapping("/deleteEvent")
    @Secured({"ROLE_MANAGER"})
    public String deleteEvent( @RequestParam Integer id )
    {
        eventService.delete( eventService.get( id ) );
        
        return "forward:listEvents";
    }
    
    @RequestMapping("/listEvents")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView listCategories()
    {
        return new ModelAndView( "maintenance/events" ).addObject( "events", sort( eventService.getAll(), new TagNameComparator<Event>() ) );
    }
}
