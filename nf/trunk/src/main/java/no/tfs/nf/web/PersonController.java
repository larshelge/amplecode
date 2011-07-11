package no.tfs.nf.web;

import static no.tfs.nf.util.CollectionUtilsWrapper.sort;

import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.comparator.TagNameComparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PersonController
{
    @Autowired
    private PersonService personService;

    @RequestMapping("/person")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView getPerson( @RequestParam(value="id",required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/person" );
        
        if ( id != null )
        {
            mav.addObject( "person", personService.get( id ) );
        }
        
        return mav;
    }

    @RequestMapping("/savePerson")
    @Secured({"ROLE_MANAGER"})
    public String savePerson( @ModelAttribute("person") Person person, BindingResult result )
    {
        if ( person.getId() > 0 )
        {
            personService.update( person );
        }
        else
        {
            personService.save( person );
        }
        
        return "forward:listPersons";
    }
    
    @RequestMapping("/deletePerson")
    @Secured({"ROLE_MANAGER"})
    public String deletePerson( @RequestParam Integer id )
    {
        personService.delete( personService.get( id ) );
        
        return "forward:listPersons";
    }
    
    @RequestMapping("/listPersons")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView listPersons()
    {
        return new ModelAndView( "maintenance/persons" ).addObject( "persons", sort( personService.getAll(), new TagNameComparator<Person>() ) );
    }
}
