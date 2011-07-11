package no.tfs.nf.service;

import java.util.Collection;

import no.tfs.nf.api.Event;
import no.tfs.nf.api.EventDao;
import no.tfs.nf.api.EventService;
import no.tfs.nf.util.DefaultGenericService;
import no.tfs.nf.util.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultEventService
    extends DefaultGenericService<Event> implements EventService
{
    @Autowired
    private EventDao eventDao;
    
    @Override
    public int save( Event event )
    {
        event.setLogicalName();
        event.setCode( event.getCode() == null || event.getCode().trim().isEmpty() ? UuidUtils.getEventUuid() : event.getCode() );
        
        return eventDao.save( event );
    }
    
    @Override
    public void update( Event event )
    {
        event.setLogicalName();
        
        eventDao.update( event );
    }
    
    @Override
    public EventDao getGenericDao()
    {
        return eventDao;
    }
    
    @Override
    public Collection<Event> getLikeName( String name )
    {
        return eventDao.getLikeName( name );
    }
}
