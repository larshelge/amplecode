package no.tfs.nf.web;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import no.tfs.nf.api.ImportService;
import no.tfs.nf.svx.Svx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImportController
{
    private static final Log log = LogFactory.getLog( ImportController.class );
    
    private static final String SUCCESS = "success";
    
    @Autowired
    private ImportService importService;
    
    @RequestMapping("/import")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView import_()
    {
        return new ModelAndView( "maintenance/import" );
    }
    
    @RequestMapping(value="/importStream",method=RequestMethod.POST)
    //@Secured({"ROLE_MANAGER"})
    public @ResponseBody String importStream( @RequestBody Svx svx )
        throws IOException
    {
        importService.importSvx( svx );
        
        return SUCCESS;
    }
    
    @RequestMapping(value="/importArchive",method=RequestMethod.POST)
    @Secured({"ROLE_MANAGER"})
    public String uploadArchive( @RequestParam("file") MultipartFile multipart )
        throws IOException
    {
        log.info( "Multipart name: " + multipart.getOriginalFilename() + ", content type " + multipart.getContentType() );
        
        File file = File.createTempFile( "IMPORT_", "_ZIP" );
        
        multipart.transferTo( file );
        
        importService.importFile( new ZipFile( file ) );
        
        return "forward:import";
    }
    
    @RequestMapping(value="/debug",method=RequestMethod.POST)
    public @ResponseBody String debug( @RequestBody String payload )
    {
        log.info( payload );
        
        return SUCCESS;
    }
}
