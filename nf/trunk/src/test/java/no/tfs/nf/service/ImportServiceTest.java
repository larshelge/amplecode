package no.tfs.nf.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.ZipFile;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.ExportService;
import no.tfs.nf.api.ImportService;
import no.tfs.nf.api.Person;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.svx.Svx;
import no.tfs.nf.svx.XClip;
import no.tfs.nf.svx.XEvent;
import no.tfs.nf.util.UrlUtils;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class ImportServiceTest
{
    private static final String TEST_OUTPUT_LOCATION = System.getProperty( "user.home" ) + "/nf-test-dir/";
    
    @Autowired
    private ExportService exportService;
    
    @Autowired
    private ImportService importService;
    
    @Autowired
    private TeamService teamService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private PersonService personService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private ClipService clipService;
    
    private Team barcelona;
    private Team arsenal;
    private Category etablert;
    private Category heading;
    private Category overgang;
    private Category utsidetouch;
    private Person xavihernandez;
    private Person lionelmessi;
    
    @BeforeClass
    public static void beforeClass()
        throws Exception
    {
        FileUtils.deleteDirectory( new File( TEST_OUTPUT_LOCATION ) );
        FileUtils.forceMkdir( new File( TEST_OUTPUT_LOCATION ) );
    }
    
    @Before
    public void before()
        throws Exception
    {
        barcelona = new Team( "barcelona", "Barcelona" );
        arsenal = new Team( "arsenal", "Arsenal" );
        etablert = new Category( "etablert", "Etablert", false );
        heading = new Category( "heading", "Heading", false );
        overgang = new Category( "overgang", "Overgang", false );
        utsidetouch = new Category( "utsidetouch", "Utsidetouch", false );
        xavihernandez = new Person( "xavihernandez", "Xaxi Hernandez" );
        lionelmessi = new Person( "lionelmessi", "Lionel Messi" );
        
        teamService.save( barcelona );
        teamService.save( arsenal );
        categoryService.save( etablert );
        categoryService.save( heading );
        categoryService.save( overgang );
        categoryService.save( utsidetouch );
        personService.save( xavihernandez );
        personService.save( lionelmessi );
    }
    
    @Test
    @Ignore //TODO
    public void testMarshal()
        throws Exception
    {
        exportService.exportMeta( new FileOutputStream( new File( TEST_OUTPUT_LOCATION + "svx-meta-test.xml" ) ) );
    }
    
    @Test
    @Ignore //TODO
    public void testUnmarshal()
        throws Exception
    {
        Svx svx = importService.unmarshal( new ClassPathResource( "svx-sample.xml" ).getInputStream() );
        
        assertNotNull( svx );
        assertEquals(1 , svx.getEvents().getEvent().size() );
        assertEquals( 5, svx.getClips().getClip().size() );
        
        Iterator<XEvent> events = svx.getEvents().getEvent().iterator();
        XEvent event = events.next();
        assertEquals( "1", event.getCode() );
        assertEquals( "London", event.getLocation() );
        assertNotNull( event.getDate() );
        assertEquals( "arsenal", event.getHomeTeam() );
        assertEquals( "barcelona", event.getAwayTeam() );
        
        Iterator<XClip> clips = svx.getClips().getClip().iterator();
        XClip clip = clips.next();
        assertEquals( 4, clip.getStart() );
        assertEquals( "barcelona", clip.getTeam() );
        assertEquals( "v101.mp4", clip.getFilename() );
        assertEquals( "1", clip.getEvent() );
    }
    
    @Test
    @Ignore //TODO
    public void testImport()
        throws Exception
    {
        UrlUtils.setProperty( UrlUtils.KEY_STATIC_LOCATION, TEST_OUTPUT_LOCATION );
        
        importService.importFile( new ZipFile( new ClassPathResource( "svx.zip" ).getFile() ) );
                
        Collection<Event> events = eventService.getAll();
        
        assertNotNull( events );
        assertEquals( 1, events.size() );
        
        Collection<Clip> clips = clipService.getAll();
        
        assertNotNull( clips );
        assertEquals( 5, clips.size() );
    }
}
