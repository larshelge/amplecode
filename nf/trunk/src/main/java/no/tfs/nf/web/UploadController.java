package no.tfs.nf.web;

import static no.tfs.nf.util.CollectionUtilsWrapper.sort;
import static no.tfs.nf.util.FileUtils.getExtension;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipCategory;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Document;
import no.tfs.nf.api.DocumentService;
import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.api.Type;
import no.tfs.nf.api.User;
import no.tfs.nf.comparator.TagNameComparator;
import no.tfs.nf.util.FileUtils;
import no.tfs.nf.util.UrlUtils;
import no.tfs.nf.util.UuidUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadController
{
    private static final Log log = LogFactory.getLog( UploadController.class );
    
    @Autowired
    private ClipService clipService;
    
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PersonService personService;

    @Autowired
    private DocumentService documentService;
    
    @RequestMapping("/videoUpload")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView videUpload( @RequestParam(required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/uploadVideo" );
        
        if ( id != null )
        {
            mav.addObject( "clip", clipService.get( id ) );            
        }
        
        mav.addObject( "teams", sort( teamService.getAll(), new TagNameComparator<Team>() ) );
        mav.addObject( "events", sort( eventService.getAll(), new TagNameComparator<Event>() ) );
        
        return mav;
    }
    
    @RequestMapping("/documentUpload")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView documentUpload( @RequestParam(required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/uploadDocument" ).addObject( "userroles", Arrays.asList( User.USERROLES ) );
        
        if ( id != null )
        {
            mav.addObject( "document", documentService.get( id ) );
        }
        
        return mav;
    }

    @RequestMapping(value="/uploadVideo",method=RequestMethod.POST)
    @Secured({"ROLE_MANAGER"})
    public String uploadVideo( @RequestParam("file") MultipartFile multipart, @ModelAttribute("clip") Clip clip, BindingResult result, @RequestParam Collection<String> categoryIds, 
        @RequestParam Collection<String> personIds, @RequestParam(required=false) Integer teamId, @RequestParam(required=false) Integer eventId, @RequestParam(required=false) String description ) throws IOException
    {
        clip.setType( Type.REFERENCE );
        
        for ( String categoryId : categoryIds )
        {
            String[] clipCategoryId = categoryId.split( ClipCategory.ID_SEPARATOR );
            Assert.isTrue( clipCategoryId != null && clipCategoryId.length == 2 );
            
            Category category = categoryService.get( Integer.valueOf( clipCategoryId[0] ) );
            int grade = Integer.valueOf( clipCategoryId[1] );
            
            clip.getClipCategories().add( new ClipCategory( category, grade ) );
            
            log.info( "Added category: " + category + " with grade: " + grade );
        }
        
        for ( String personId : personIds )
        {
            clip.getPersons().add( personService.get( Integer.valueOf( personId ) ) );
        }

        if ( teamId != null )
        {
            clip.setTeam( teamService.get( teamId ) );
        }
        
        if ( eventId != null )
        {
            clip.setEvent( eventService.get( eventId ) );
        }

        if ( !multipart.isEmpty() )
        {
            log.info( "Multipart name: " + multipart.getOriginalFilename() + ", content type: " + multipart.getContentType() );
                    
            String filename = UuidUtils.getClipUuid() + getExtension( multipart.getOriginalFilename() );
            String path = UrlUtils.staticLocation() + filename;
            
            multipart.transferTo( new File( path ) );
            clip.setFilename( filename );
            
            log.info( "Imported video file to: " + path );
        }

        if ( clip.getId() > 0 )
        {
            clipService.update( clip );
        }
        else
        {
            clipService.save( clip );
        }
        
        log.info( "Saved clip, team: " + clip.getTeam() + ", event: " + clip.getEvent() );
        
        return "forward:videoUpload";
    }
    
    @RequestMapping(value="/deleteVideo")
    @Secured({"ROLE_MANAGER"})
    public String deleteVideo( @RequestParam Integer id, @RequestParam(required=false) String backUrl )
    {
        Clip clip = clipService.get( id );
        
        if ( clip != null )
        {
            String path = UrlUtils.staticLocation() + clip.getFilename();
            
            FileUtils.deleteFile( path );
            
            clipService.delete( clip );
        }
        
        return "forward:" + StringUtils.defaultIfEmpty( backUrl, "references" );
    }

    @RequestMapping(value="/uploadDocument",method=RequestMethod.POST)
    @Secured({"ROLE_MANAGER"})
    public String uploadDocument( @RequestParam("file") MultipartFile multipart, @ModelAttribute("document") Document document, BindingResult result, 
        @RequestParam Collection<String> categories, @RequestParam String description, @RequestParam String requiredAuthority ) throws IOException
    {
        if ( !multipart.isEmpty() )
        {
            log.info( "Multipart name: " + multipart.getOriginalFilename() + ", content type: " + multipart.getContentType() );
            
            String filename = UuidUtils.getDocumentUuid() + getExtension( multipart.getOriginalFilename() );
            String path = UrlUtils.staticLocation() + filename;        
    
            multipart.transferTo( new File( path ) );
            document.setPath( filename );
    
            log.info( "Imported document to: " + path );
        }
        
        requiredAuthority = StringUtils.trimToNull( requiredAuthority );
        
        for ( String categoryId : categories )
        {
            document.getCategories().add( categoryService.get( Integer.valueOf( categoryId ) ) );
        }
        
        document.setDescription( description );
        document.setRequiredAuthority( requiredAuthority );
        
        if ( document.getId() > 0 )
        {
            documentService.update( document );
        }
        else
        {
            documentService.save( document );
        }
        
        return "forward:documentUpload";
    }
    
    @RequestMapping(value="/deleteDocument")
    @Secured({"ROLE_MANAGER"})
    public String deleteDocument( @RequestParam Integer id, @RequestParam(required=false) String backUrl )
    {
        Document document = documentService.get( id );
        
        if ( document != null )
        {
            String path = UrlUtils.staticLocation() + document.getPath();
            
            FileUtils.deleteFile( path );
            
            documentService.delete( document );
        }
        
        return "forward:" + StringUtils.defaultIfEmpty( backUrl, "references" );
    }
}
