package no.tfs.nf.service;

import static no.tfs.nf.util.FileUtils.directoryIsValid;
import static no.tfs.nf.util.FileUtils.getExtension;
import static no.tfs.nf.util.FileUtils.isAcceptedVideoFile;
import static no.tfs.nf.util.FileUtils.throwIOException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.transform.stream.StreamSource;

import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventService;
import no.tfs.nf.api.ImportService;
import no.tfs.nf.api.PersonService;
import no.tfs.nf.api.TeamService;
import no.tfs.nf.api.Type;
import no.tfs.nf.svx.Svx;
import no.tfs.nf.svx.XClip;
import no.tfs.nf.svx.XEvent;
import no.tfs.nf.util.UrlUtils;
import no.tfs.nf.util.UuidUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

@Service
public class DefaultImportService
    implements ImportService
{
    private static final Log log = LogFactory.getLog( DefaultImportService.class );
    
    private static final String NAME_DATAFILE = "data.xml";
    
    @Autowired
    private Jaxb2Marshaller marshaller;
    
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
    
    @Override
    public void importSvx( Svx svx )
        throws IOException
    {
        log.info( "Found " + svx.getClips().getClip().size() + " clips in import file" );

        Map<String, String> filenameMap = getFilenameMap( svx );

        log.info( "Created filename mapping" );
        
        importVideoFiles( svx, filenameMap );

        log.info( "Imported video files" );
        
        importData( svx, filenameMap );
        
        log.info( "Import done" );
    }
    
    @Override
    public void importFile( ZipFile file )
        throws IOException
    {
        boolean outputLocationIsValid = directoryIsValid( new File( UrlUtils.staticLocation() ) );
        
        throwIOException( !outputLocationIsValid, "Output location is not valid" );
        
        InputStream in = getDataStream( file );
        
        throwIOException( in == null, "No data file found in archive" );
        
        Svx svx = unmarshal( in );
        
        log.info( "Found " + svx.getClips().getClip().size() + " clips in import file" );

        Map<String, String> filenameMap = getFilenameMap( svx );
        
        log.info( "Created filename mapping" );
        
        importVideoFiles( file, filenameMap );
        
        log.info( "Imported video files" );
        
        importData( svx, filenameMap );
        
        log.info( "Import done" );
    }

    @Override
    public Svx unmarshal( InputStream in )
    {
        try
        {
            return (Svx) marshaller.unmarshal( new StreamSource( in ) );
        }
        finally
        {
            IOUtils.closeQuietly( in );
        }
    }
    
    /**
     * Persists the Event and Clip objects found in the Svx instance.
     */
    private void importData( Svx svx, Map<String, String> filenameMap )
    {
        XEvent e = svx.getEvent();
        
        Event event = new Event().fromX( e );
        event.setHomeTeam( teamService.getByCode( e.getHomeTeam() ) );
        event.setAwayTeam( teamService.getByCode( e.getAwayTeam() ) );
        event.setCode( UuidUtils.getEventUuid() );
        event.setLogicalName();
        
        eventService.save( event );
        
        log.info( "Imported event " + event );

        for ( XClip c : svx.getClips().getClip() )
        {
            Clip clip = new Clip().fromX( c );
            clip.setType( Type.FEEDBACK );
            clip.setTeam( teamService.getByCode( c.getTeam() ) );
            clip.setEvent( event );
            clip.setFilename( filenameMap.get( c.getFilename() ) );
            
            if ( c.getCategories() != null && c.getCategories().getCategory() != null )
            {
                for ( String category : c.getCategories().getCategory() )
                {
                    clip.addCategory( categoryService.getByCode( category ) );
                }
            }
            
            if ( c.getPersons() != null && c.getPersons().getPerson() != null )
            {
                for ( String person : c.getPersons().getPerson() )
                {
                    clip.addPerson( personService.getByCode( person ) );
                }
            }
            
            clipService.save( clip );
            
            log.info( "Imported clip " + clip );
        }
        
        log.info( "Imported clips" );
    }
    
    private void importVideoFiles( Svx svx, Map<String, String> filenameMap )
        throws IOException
    {
        for ( String filename : filenameMap.keySet() )
        {
            if ( isAcceptedVideoFile( filename ) )
            {
                String source = UrlUtils.uploadLocation() + filename;
                String target = UrlUtils.staticLocation() + filenameMap.get( filename );
                
                log.info( "Starting import of file, source: '" + source + "', target: '" + target + "'" );
                
                BufferedInputStream in = null;
                BufferedOutputStream out = null;
                
                try
                {
                    try
                    {
                        in = new BufferedInputStream( new FileInputStream( source ) );
                    }
                    catch ( FileNotFoundException ex )
                    {
                        log.error( "File was not found: '" + source + "'" );
                    }
                    
                    out = new BufferedOutputStream( new FileOutputStream( target ) );
                    
                    IOUtils.copy( in, out );                    

                    log.info( "Imported file successfully, source: '" + source + "', target: '" + target + "'" );
                }
                finally
                {
                    IOUtils.closeQuietly( in );
                    IOUtils.closeQuietly( out );
                }
            }
        }
    }
    
    /**
     * Identifies the video streams inside the given zip file and writes them
     * to the output location.
     */
    private void importVideoFiles( ZipFile file, Map<String, String> filenameMap )
        throws IOException
    {
        Enumeration<? extends ZipEntry> entries = file.entries();

        while ( entries.hasMoreElements() )
        {
            ZipEntry entry = entries.nextElement();
            
            String filename = entry.getName();
            
            if ( isAcceptedVideoFile( filename ) )
            {
                String target = UrlUtils.staticLocation() + filenameMap.get( filename );
                
                log.info( "Found file '" + filename + "'" );
                
                BufferedOutputStream out = null;
                
                try
                {
                    out = new BufferedOutputStream( new FileOutputStream( target ) );
                    
                    IOUtils.copy( file.getInputStream( entry ), out );
                    
                    log.info( "Wrote file to '" + target + "'" );
                }
                finally
                {
                    IOUtils.closeQuietly( out );
                }
            }
        }
    }

    /**
     * Creates a mapping between the physical filenames on the Clip objects in
     * the Svx instance and generated random unique names. The mapping holds
     * one key per file.
     */
    private Map<String, String> getFilenameMap( Svx svx )
    {
        Map<String, String> filenameMap = new HashMap<String, String>();

        for ( XClip c : svx.getClips().getClip() )
        {
            filenameMap.put( c.getFilename(), UuidUtils.getClipUuid() + getExtension( c.getFilename() ) );
        }
        
        return filenameMap;
    }
    
    /**
     * Identifies and returns the stream that contains the data message inside
     * the given zip file.
     */
    private InputStream getDataStream( ZipFile file )
        throws IOException
    {
        InputStream in = null;
        Enumeration<? extends ZipEntry> entries = file.entries();

        while ( entries.hasMoreElements() )
        {
            ZipEntry entry = entries.nextElement();

            log.info( "Inspecting entry " + entry.getName() );
            
            if ( entry != null && entry.getName().equals( NAME_DATAFILE ) )
            {
                in = file.getInputStream( entry );
            }
        }
        
        return in;
    }
}
