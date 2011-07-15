package no.tfs.nf.util;

import java.util.Collection;
import java.util.List;

import no.tfs.nf.api.GenericDao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class HibernateGenericDao<T>
    implements GenericDao<T>
{
    @Autowired
    protected SessionFactory sessionFactory;
    
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    private Class<T> clazz;

    /**
     * Could be overridden programmatically.
     */
    protected Class<T> getClazz()
    {
        return clazz;
    }

    /**
     * Could be injected through container.
     */
    public void setClazz( Class<T> clazz )
    {
        this.clazz = clazz;
    }
    
    // -------------------------------------------------------------------------
    // Convenience methods
    // -------------------------------------------------------------------------

    /**
     * Creates a Query.
     * 
     * @param hql the HQL query.
     * @return a Query instance.
     */
    protected final Query getQuery( String hql )
    {
        return sessionFactory.getCurrentSession().createQuery( hql );
    }
    
    /**
     * Creates a Criteria for the implementation Class type.
     * 
     * @return a Criteria instance.
     */
    protected final Criteria getCriteria()
    {
        return sessionFactory.getCurrentSession().createCriteria( getClazz() );
    }
    
    /**
     * Creates a Criteria for the implementation Class type restricted by the
     * given Criteria.
     * 
     * @param expressions the Criteria for the Criteria.
     * @return a Criteria instance.
     */
    protected final Criteria getCriteria( Criterion... expressions )
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( getClazz() );
        
        for ( Criterion expression : expressions )
        {
            criteria.add( expression );
        }
        
        return criteria;
    }
    
    /**
     * Retrieves an object based on the given Criteria.
     * 
     * @param expressions the Criteria for the Criteria.
     * @return an object of the implementation Class type.
     */
    @SuppressWarnings( "unchecked" )
    protected final T getObject( Criterion... expressions )
    {
        return (T) getCriteria( expressions ).uniqueResult();
    }
    
    /**
     * Retrieves a List based on the given Criteria.
     * 
     * @param expressions the Criteria for the Criteria.
     * @return a List with objects of the implementation Class type.
     */
    @SuppressWarnings( "unchecked" )
    protected final List<T> getList( Criterion... expressions )
    {
        return getCriteria( expressions ).list();
    }

    // -------------------------------------------------------------------------
    // GenericDao implementation
    // -------------------------------------------------------------------------

    @Override
    public int save( T object )
    {
        return (Integer) sessionFactory.getCurrentSession().save( object );
    }

    @Override
    public void update( T object )
    {
        sessionFactory.getCurrentSession().update( object );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public T get( int id )
    {
        return (T) sessionFactory.getCurrentSession().get( getClazz(), id );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public T load( int id )
    {
        return (T) sessionFactory.getCurrentSession().load( getClazz(), id );
    }

    public T getByCode( String code )
    {
        return getObject( Restrictions.eq( "code", code ) );
    }

    public T getByName( String name )
    {
        return getObject( Restrictions.eq( "name", name ) );
    }

    @SuppressWarnings("unchecked")
    public Collection<T> getLikeName( String name )
    {
        return getCriteria( Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase() ).list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<T> getAll()
    {
        return getCriteria().list();
    }

    @Override
    public void delete( T object )
    {
        sessionFactory.getCurrentSession().delete( object );
    }
}
