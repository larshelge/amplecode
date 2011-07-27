package no.tfs.nf.web;

import static no.tfs.nf.util.CollectionUtilsWrapper.sort;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.comparator.TagNameComparator;

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
public class TeamController
{
    @Autowired
    private TeamService teamService;

    @RequestMapping("/team")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView getTeam( @RequestParam(value="id",required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/team" );
        
        if ( id != null )
        {
            mav.addObject( "team", teamService.get( id ) );
        }
        
        return mav;
    }

    @RequestMapping("/saveTeam")
    @Secured({"ROLE_MANAGER"})
    public String saveTeam( @ModelAttribute("team") Team team, BindingResult result )
    {
        if ( team.getId() > 0 )
        {
            teamService.update( team );
        }
        else
        {
            teamService.save( team );
        }
        
        return "forward:listTeams";
    }
    
    @RequestMapping("/deleteTeam")
    @Secured({"ROLE_MANAGER"})
    public String deleteEvent( @RequestParam Integer id )
    {
        teamService.delete( teamService.get( id ) );
        
        return "forward:listTeams";
    }
    
    @RequestMapping("/listTeams")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView listTeams()
    {
        return new ModelAndView( "maintenance/teams" ).addObject( "teams", sort( teamService.getAll(), new TagNameComparator<Team>() ) );
    }

    @RequestMapping("/teamCodeAvailable")
    public @ResponseBody Boolean codeAvailable( @RequestParam String code, @RequestParam(value="id",required=false) Integer id )
    {
        if ( id != null ) // Update
        {
            Team team = teamService.get( id );
            
            if ( team != null && team.getCode().trim().equals( code.trim() ) )
            {
                return true;
            }
        }
        
        return teamService.getByCode( code ) == null;
    }
}
