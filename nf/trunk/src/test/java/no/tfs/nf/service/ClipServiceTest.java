package no.tfs.nf.service;

import static junit.framework.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryDao;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipCategory;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.api.Type;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class ClipServiceTest
{
    @Autowired
    private ClipService clipService;

    @Autowired
    private CategoryDao categoryService;

    @Autowired
    private TeamService teamService;
    
    @Autowired
    private EventService eventService;

    @Autowired
    private PersonService personService;

    private Set<ClipCategory> clipCategoriesA = new HashSet<ClipCategory>();
    private Set<ClipCategory> clipCategoriesB = new HashSet<ClipCategory>();
    
    private Set<Person> personsA = new HashSet<Person>();
    
    private Category categoryA = new Category( "categorya", "CategoryA", true );
    private Category categoryB = new Category( "categoryb", "CategoryB", true );
    private Category categoryC = new Category( "categoryc", "CategoryC", true );
    private Category categoryD = new Category( "categoryd", "CategoryD", true );

    private Person personA = new Person( "persona", "PersonA" );
    private Person personB = new Person( "personb", "PersonB" );
    
    private Team teamA = new Team( "teama", "TeamA" );
    private Team teamB = new Team( "teamb", "TeamB" );
    private Event eventA = new Event( "eventa", "EventA", "location", null );
    private Event eventB = new Event( "eventb", "EventB", "location", null );
    
    @Before
    public void before()
    {
        categoryService.save( categoryA );
        categoryService.save( categoryB );
        categoryService.save( categoryC );
        categoryService.save( categoryD );

        clipCategoriesA.add( new ClipCategory( categoryA ) );
        clipCategoriesA.add( new ClipCategory( categoryB ) );
        clipCategoriesB.add( new ClipCategory( categoryC ) );
        clipCategoriesB.add( new ClipCategory( categoryD ) );
        
        personsA.add( personA );
        personsA.add( personB );
        
        personService.save( personA );
        personService.save( personB );
        
        teamService.save( teamA );
        teamService.save( teamB );
        
        eventService.save( eventA );
        eventService.save( eventB );
    }
    
    @Test
    public void testSave()
    {
        Clip clipA = new Clip( "cA", "filenameA", clipCategoriesA, personsA, null, teamA, eventA, Type.FEEDBACK );
        Clip clipB = new Clip( "cB", "filenameB", clipCategoriesB, personsA, null, teamA, eventA, Type.FEEDBACK );
        
        int idA = clipService.save( clipA );
        int idB = clipService.save( clipB );
                
        assertNotNull( clipService.get( idA ) );
        assertNotNull( clipService.get( idB ) );
        
        assertEquals( clipA, clipService.get( idA ) );
        assertEquals( clipB, clipService.get( idB ) );
    }

    @Test
    public void testGetByCode()
    {
        Clip clipA = new Clip( "cA", "filenameA", clipCategoriesA, personsA, null, teamA, eventA, Type.FEEDBACK );
        Clip clipB = new Clip( "cB", "filenameB", clipCategoriesB, personsA, null, teamA, eventA, Type.FEEDBACK );
        
        clipService.save( clipA );
        clipService.save( clipB );
        
        assertEquals( clipA, clipService.getByCode( "cA" ) );
        assertEquals( clipB, clipService.getByCode( "cB" ) );
    }
    
    @Test
    @Ignore //TODO transaction not comitted?
    public void getByCategoryEventPersonType()
    {
        Clip clipA = new Clip( "cA", "filenameA", clipCategoriesA, personsA, null, teamA, eventA, Type.FEEDBACK );
        Clip clipB = new Clip( "cB", "filenameB", clipCategoriesB, personsA, null, teamA, eventA, Type.FEEDBACK );
        Clip clipC = new Clip( "cC", "filenameC", clipCategoriesA, personsA, null, teamA, eventB, Type.FEEDBACK );
        Clip clipD = new Clip( "cD", "filenameD", clipCategoriesB, personsA, null, teamA, eventB, Type.FEEDBACK );        
        Clip clipE = new Clip( "cE", "filenameE", clipCategoriesA, personsA, null, teamA, eventA, Type.FEEDBACK );
        Clip clipF = new Clip( "cF", "filenameF", clipCategoriesB, personsA, null, teamA, eventA, Type.FEEDBACK );
        
        clipService.save( clipA );
        clipService.save( clipB );
        clipService.save( clipC );
        clipService.save( clipD );
        clipService.save( clipE );
        clipService.save( clipF );
        
        Collection<Clip> clips = clipService.get( "CategoryA", "EventA", "PersonA", null );
        
        assertEquals( 2, clips.size() );
        assertTrue( clips.contains( clipA ) );
        assertTrue( clips.contains( clipE ) );
        
        clips = clipService.get( "CategoryA", "", "", "" );
        
        assertEquals( 3, clips.size() );
        assertTrue( clips.contains( clipA ) );
        assertTrue( clips.contains( clipC ) );
        assertTrue( clips.contains( clipE ) );
        
        clips = clipService.get( "", "", "", "" );
        
        assertEquals( 6, clips.size() );
    }
}
