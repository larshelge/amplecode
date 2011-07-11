package no.tfs.nf.web;

import static no.tfs.nf.util.UrlUtils.PARAM_SUGGESTIONS;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.PlaylistService;
import no.tfs.nf.api.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TagController
{
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private PersonService personService;
    
    @Autowired
    private PlaylistService playlistService;
    
    @Autowired
    private TagService tagService;
        
    @RequestMapping("/references")
    public ModelAndView references()
    {
        return new ModelAndView( "references" );
    }
    
    @RequestMapping("/feedback")
    @Secured({"ROLE_USER"})
    public ModelAndView feedback()
    {
        return new ModelAndView( "feedback" );
    }

    @RequestMapping("/tags")
    public ModelAndView getCategoriesTeamsPersons( @RequestParam String term )
    {
        ModelAndView mav = new ModelAndView( "suggestions" );
        
        mav.addObject( PARAM_SUGGESTIONS, tagService.getCategoryTeamPersonLikeName( term ) );
        
        return mav;
    }

    @RequestMapping("/eventsteams")
    public ModelAndView getEventsTeams( @RequestParam String term )
    {
        ModelAndView mav = new ModelAndView( "suggestions" );
        
        mav.addObject( PARAM_SUGGESTIONS, tagService.getEventTeamLikeName( term ) );
        
        return mav;
    }

    @RequestMapping("/persons")
    public ModelAndView getPersons( @RequestParam String term )
    {
        ModelAndView mav = new ModelAndView( "suggestions" );
        
        mav.addObject( PARAM_SUGGESTIONS, personService.getLikeName( term ) );
        
        return mav;
    }

    @RequestMapping("/categories")
    public ModelAndView getCategories( @RequestParam String term )
    {
        ModelAndView mav = new ModelAndView( "suggestions" );
        
        if ( term.equals( ":standard" ) )
        {
            mav.addObject( PARAM_SUGGESTIONS, categoryService.getStandard() );
        }
        else if ( term.equals( ":all" ) )
        {
            mav.addObject( PARAM_SUGGESTIONS, categoryService.getAll() );
        }
        else
        {
            mav.addObject( PARAM_SUGGESTIONS, categoryService.getLikeName( term ) );
        }
        
        return mav;
    }
    
    @RequestMapping("/playlists")
    public ModelAndView getPlaylists( @RequestParam String term )
    {
        ModelAndView mav = new ModelAndView( "suggestions" );
        
        if ( term.equals( ":myown" ) )
        {
            mav.addObject( PARAM_SUGGESTIONS, playlistService.getOwnedByCurrentUser() );
        }
        else if ( term.equals( ":shared" ) )
        {
            mav.addObject( PARAM_SUGGESTIONS, playlistService.getSharedWithCurrentUser() );
        }
        else
        {
            mav.addObject( PARAM_SUGGESTIONS, playlistService.getLikeName( term ) );
        }
                
        return mav;
    }
    
    @RequestMapping("/clipcategories")
    public ModelAndView getClipCategories( @RequestParam String term )
    {
        return new ModelAndView( "clipCategories" ).addObject( PARAM_SUGGESTIONS, categoryService.getClipCategoriesLikeName( term ) );
    }
}
