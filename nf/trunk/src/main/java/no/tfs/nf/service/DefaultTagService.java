package no.tfs.nf.service;

import java.util.ArrayList;
import java.util.Collection;

import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.Tag;
import no.tfs.nf.api.TagService;
import no.tfs.nf.api.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTagService
    implements TagService
{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TeamService teamService;
    
    @Autowired
    private EventService eventService;

    @Autowired
    private PersonService personService;
    
    @Override
    public Collection<Tag> getCategoryTeamPersonLikeName( String name )
    {
        Collection<Tag> tags = new ArrayList<Tag>();
        
        tags.addAll( categoryService.getLikeName( name ) );
        tags.addAll( personService.getLikeName( name ) );
        tags.addAll( teamService.getLikeName( name ) );
        
        return tags;
    }
    
    @Override
    public Collection<Tag> getEventTeamLikeName( String name )
    {
        Collection<Tag> tags = new ArrayList<Tag>();

        tags.addAll( teamService.getLikeName( name ) );
        tags.addAll( eventService.getLikeName( name ) );
        
        return tags;
    }
}
