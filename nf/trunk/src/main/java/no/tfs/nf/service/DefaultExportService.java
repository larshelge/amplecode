package no.tfs.nf.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.stream.StreamResult;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.ExportService;
import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.svx.ObjectFactory;
import no.tfs.nf.svx.Svx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

@Service
public class DefaultExportService
    implements ExportService
{    
    @Autowired
    private Jaxb2Marshaller marshaller;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private TeamService teamService;
    
    @Autowired
    private PersonService personService;
    
    public Svx getMeta()
    {
        ObjectFactory factory = new ObjectFactory();
        Svx svx = factory.createSvx();
        svx.setCategories( factory.createSvxCategories() );
        svx.setTeams( factory.createSvxTeams() );
        svx.setPersons( factory.createSvxPersons() );
        
        for ( Category category : categoryService.getStandard() )
        {
            svx.getCategories().getCategory().add( category.toX() );
        }
        
        for ( Team team : teamService.getAll() )
        {
            svx.getTeams().getTeam().add( team.toX() );
        }
        
        for ( Person person : personService.getAll() )
        {
            svx.getPersons().getPerson().add( person.toX() );
        }
        
        return svx;
    }
    
    public void exportMeta( OutputStream out )
        throws IOException
    {
        StreamResult result = new StreamResult( out );
        
        marshaller.marshal( getMeta(), result );
    }
}
