package no.tfs.nf.util;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import no.tfs.nf.api.GenericDao;
import no.tfs.nf.api.GenericService;

@Transactional
public class DefaultGenericService<T>
    implements GenericService<T>
{
    private GenericDao<T> genericDao;
        
    public GenericDao<T> getGenericDao()
    {
        return genericDao;
    }

    public void setGenericDao( GenericDao<T> genericDao )
    {
        this.genericDao = genericDao;
    }

    @Override
    public void delete( T object )
    {
        getGenericDao().delete( object );
    }

    @Override
    public T get( int id )
    {
        return getGenericDao().get( id );
    }

    @Override
    public Collection<T> getAll()
    {
        return getGenericDao().getAll();
    }

    @Override
    public T load( int id )
    {
        return getGenericDao().load( id );
    }

    @Override
    public int save( T object )
    {
        return getGenericDao().save( object );
    }

    @Override
    public void update( T object )
    {
        getGenericDao().update( object );
    }
}
