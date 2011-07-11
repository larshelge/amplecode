package no.tfs.nf.web;

import no.tfs.nf.api.Document;
import no.tfs.nf.api.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DocumentController
{
    @Autowired
    private DocumentService documentService;
    
    @RequestMapping("/document")
    public ModelAndView getDocument( @RequestParam Integer id )
    {
        return new ModelAndView( "document" ).addObject( "document", documentService.get( id ) );
    }

    @RequestMapping("/doc")
    public @ResponseBody Document get( @RequestParam Integer id )
    {
        return documentService.get( id );
    }
}
