package no.tfs.nf.service;

import java.util.Date;
import java.util.List;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipDao;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.GenericDao;
import no.tfs.nf.api.Type;
import no.tfs.nf.util.DefaultGenericService;
import no.tfs.nf.util.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultClipService
    extends DefaultGenericService<Clip> implements ClipService
{
    @Autowired
    private ClipDao clipDao;

    @Override
    public GenericDao<Clip> getGenericDao()
    {
        return clipDao;
    }
    
    @Override
    public int save( Clip clip )
    {
        clip.setCode( clip.getCode() == null || clip.getCode().trim().isEmpty() ? UuidUtils.getClipUuid() : clip.getCode() );
        clip.setCreated( new Date() );
        
        return clipDao.save( clip );
    }
    
    @Override
    public void update( Clip clip )
    {
        clip.setCreated( new Date() );
        
        clipDao.update( clip );
    }

    @Override
    public Clip getByCode( String code )
    {
        if ( code.startsWith( UuidUtils.PREFIX_CLIP ) )
        {
            return clipDao.getByCode( code );
        }
        else if ( code.startsWith( UuidUtils.PREFIX_PLAYLIST ) )
        {
            return clipDao.getFirstInPlaylist( code );
        }
        
        throw new IllegalArgumentException( "Invalid code: " + code );
    }
    
    @Override
    public List<Clip> get( String category, String eventTeam, String person, String playlist )
    {
        return clipDao.get( category, eventTeam, person, playlist );
    }
    
    @Override
    public List<Clip> get( String q, Type type )
    {
        return clipDao.get( q, type );
    }
    
    @Override
    public List<Clip> getHierarchy( Category category )
    {
        return clipDao.getHierarchy( category );
    }
    
    @Override
    public Clip getLatest()
    {
        return clipDao.getLatest();
    }
    
    @Override
    public List<Clip> getLatest( int number )
    {
        return clipDao.getLatest( number );
    }

    @Override
    public boolean bumpViews( int id )
    {
        Clip clip = clipDao.get( id );
        
        if ( clip != null )
        {
            clip.setViews( clip.getViews() + 1 );
            
            clipDao.update( clip );
        }
        
        return clip != null;
    }
}
