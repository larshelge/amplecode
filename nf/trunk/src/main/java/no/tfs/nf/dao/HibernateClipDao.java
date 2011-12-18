package no.tfs.nf.dao;

import java.util.List;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipDao;
import no.tfs.nf.api.Type;
import no.tfs.nf.util.HibernateGenericDao;
import no.tfs.nf.util.TextUtils;
import no.tfs.nf.util.UrlUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateClipDao
    extends HibernateGenericDao<Clip> implements ClipDao
{
    //TODO include type
    
    private static final Log log = LogFactory.getLog( HibernateClipDao.class );
        
    @Override
    protected Class<Clip> getClazz()
    {
        return Clip.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Clip> get( String category, String eventTeam, String person, String playlist )
    {
        String sql = "select cl.* from clip cl left join team t on cl.team_id = t.id left join event e on cl.event_id = e.id";

        int first = 0;
        
        sql += eventTeam != null ? ( first++ == 0 ? " where" : " and" ) + " ( lower(e.name) = '" + eventTeam.trim().toLowerCase() + "' or lower(t.name) = '" + eventTeam.trim().toLowerCase() + "' )" : "";
        
        sql += playlist != null ? ( first++ == 0 ? " where" : " and" ) + " exists( select p.name from playlist p, clip_playlist cp where p.id = cp.playlists_id and cp.clip_id = cl.id and lower(p.name) = '" + 
            playlist.trim().toLowerCase() + "' )" : "";
        
        if ( category != null )
        {
            String[] categorySplit = category.split( UrlUtils.DELIMITERS );
            
            for ( String s : categorySplit )
            {
                sql += ( first++ == 0 ? " where" : " and" ) + 
                    " exists( select c.name from category c, clipcategory cc where c.id = cc.category_id and cc.clip_id = cl.id and lower(c.name) = '" + s.trim().toLowerCase() + "' )";
            }
        }
        
        sql += person != null ? ( first++ == 0 ? " where" : " and" ) +
            " exists( select pe.name from person pe, clip_person cp where pe.id = cp.persons_id and cp.clip_id = cl.id and lower(pe.name) = '" + person.trim().toLowerCase() + "' )" : "";
        
        sql += " order by cl.created desc";
                
        log.debug( sql );
        
        return sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( "clip", Clip.class ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Clip> get( String q, Type type )
    {
        String[] qSplit = q.split( UrlUtils.DELIMITERS );
        String qJoin = TextUtils.join( qSplit, ",", "'" ).toLowerCase();
        
        String sql = "select cl.*";
        
        sql += ", ( select sum( cc.grade ) from clipcategory cc, category c where cl.id = cc.clip_id and cc.category_id = c.id and lower(c.name) in ( " + qJoin + " ) ) as totalgrade";
        
        sql += " from clip cl left join team t on cl.team_id = t.id";
        
        sql += " where cl.type = '" + type.name() + "'";
        
        for ( String s : qSplit )
        {
            s = s.trim().toLowerCase();
            
            sql += " and ( lower(t.name) = '" + s + "' or" +
                " exists( select c.name from category c, clipcategory cc where c.id = cc.category_id and cc.clip_id = cl.id and lower(c.name) = '" + s + "' ) or" +
                " exists( select pe.name from person pe, clip_person cp where pe.id = cp.persons_id and cp.clip_id = cl.id and lower(pe.name) = '" + s + "' ) )";
        }

        sql += " order by totalgrade desc, cl.created desc";
        
        log.debug( sql );
        
        return sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( "clip", Clip.class ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Clip> getHierarchy( Category category )
    {
        String ids = StringUtils.join( category.getCategoryWithChildrenIds().toArray(), ',' );
        
        String sql = "select cl.* from clip cl where exists( select c.id from category c, clipcategory cc where c.id = cc.category_id and cc.clip_id = cl.id and c.id in (" + 
            ids + ") ) order by cl.created desc";
        
        log.debug( sql );
        
        return sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( "clip", Clip.class ).list();
    }
    
    @Override
    public Clip getFirstInPlaylist( String code )
    {
        String sql = "select cl.* from clip cl join clip_playlist cp on cl.id = cp.clip_id join playlist pl on cp.playlists_id = pl.id where pl.code='" + code + "' limit 1";

        log.debug( sql );
        
        return (Clip) sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( "clip", Clip.class ).uniqueResult();
    }
    
    @Override
    public Clip getLatest()
    {
        return (Clip) getCriteria( Restrictions.eq( "type", Type.REFERENCE ) ).addOrder( Order.desc( "created" ) ).setMaxResults( 1 ).uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Clip> getLatest( int number )
    {
        return getCriteria( Restrictions.eq( "type", Type.REFERENCE ) ).addOrder( Order.desc( "created" ) ).setMaxResults( number ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Clip> getByPlaylist( String code )
    {             
        String sql = "select cl.* from clip cl join clip_playlist cp on cl.id = cp.clip_id join playlist pl on cp.playlists_id = pl.id where pl.code='" + code + "' order by cl.created desc";
        
        log.debug( sql );
        
        return sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( "clip", Clip.class ).list();            
    }
}
