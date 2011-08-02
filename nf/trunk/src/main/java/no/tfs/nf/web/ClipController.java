package no.tfs.nf.web;

import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.ArrayList;
import java.util.List;

import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Document;
import no.tfs.nf.api.DocumentService;
import no.tfs.nf.api.Type;
import no.tfs.nf.api.UserService;
import no.tfs.nf.util.Paging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClipController
{
    private final static String TYPE_VIDEO = "video";
    private final static String TYPE_DOCUMENT = "document";
    
    @Autowired
    private ClipService clipService;

    @Autowired
    private DocumentService documentService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/clip")
    public @ResponseBody Clip getById( @RequestParam Integer id )
    {
        return clipService.get( id );
    }
    
    @RequestMapping("/play")
    public ModelAndView play( @RequestParam String code )
    {
        return new ModelAndView( "references" ).addObject( "code", code );
    }

    @RequestMapping("/rsearch")
    public ModelAndView referenceSearch( @RequestParam String query, @RequestParam(required=false) String queryType, @RequestParam(required=false) Integer page )
    {
        int currentPage = page == null ? 1 : page;
        
        ModelAndView mav = new ModelAndView( "clips" ).addObject( "query", query );

        query = trimToNull( query );
        
        if ( query != null )
        {
            if ( queryType == null || TYPE_VIDEO.equals( queryType ) )
            {
                List<Clip> clips = new ArrayList<Clip>( clipService.get( query, Type.REFERENCE ) );
                
                Paging paging = new Paging( currentPage, clips.size() );
                
                mav.addObject( "clips", clips.subList( paging.getStartPos(), paging.getEndPos() ) );
                mav.addObject( "paging", paging );
            }
            else if ( TYPE_DOCUMENT.equals( queryType ) )
            {
                List<Document> docs = new ArrayList<Document>( documentService.get( query, userService.getCurrentUser() ) );

                Paging paging = new Paging( currentPage, docs.size() );
                
                mav.addObject( "docs", docs.subList( paging.getStartPos(), paging.getEndPos() ) );
                mav.addObject( "paging", paging );
            }
        }
        
        return mav;
    }
    
    @RequestMapping("/fsearch")
    public ModelAndView feedbackSearch( 
        @RequestParam(value="eventteam",required=false) String eventTeam,
        @RequestParam(value="person",required=false) String person,
        @RequestParam(value="category",required=false) String category,
        @RequestParam(value="playlist",required=false) String playlist )
    {
        ModelAndView mav = new ModelAndView( "clips" );

        eventTeam = trimToNull( eventTeam );
        category = trimToNull( category );
        person = trimToNull( person );
        playlist = trimToNull( playlist );
        
        if ( !( category == null && eventTeam == null && person == null && playlist == null ) )
        {
            mav.addObject( "clips", clipService.get( category, eventTeam, person, playlist ) );
        }
        
        return mav;
    }

    @RequestMapping("/hsearch")
    public ModelAndView hierarchySearch( @RequestParam Integer id )
    {
        ModelAndView mav = new ModelAndView( "clips" );
        
        if ( id != null )
        {
            mav.addObject( "clips", clipService.getHierarchy( categoryService.get( id ) ) );
        }
        
        return mav;
    }
    
    @RequestMapping("/aclip")
    public @ResponseBody Clip getByCode( @RequestParam String code )
    {
        return clipService.getByCode( code );
    }
    
    @RequestMapping("/asearch")
    public ModelAndView associationSearch( @RequestParam String code )
    {
        return new ModelAndView( "clips" ).addObject( "clips", clipService.getAssociations( code ) );
    }

    @RequestMapping("/latestClipId")
    public @ResponseBody Integer latestClipId()
    {
        Clip clip = clipService.getLatest();
        
        return clip != null ? clip.getId() : 0;
    }
    
    @RequestMapping("/latestClips")
    public ModelAndView latestClips()
    {
        return new ModelAndView( "clips" ).addObject( "clips", clipService.getLatest( Paging.DEFAULT_PAGE_SIZE ) );
    }
}
