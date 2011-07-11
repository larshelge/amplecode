package no.tfs.nf.dao;

import java.util.Collection;
import java.util.List;

import no.tfs.nf.api.Playlist;
import no.tfs.nf.api.PlaylistDao;
import no.tfs.nf.api.User;
import no.tfs.nf.util.HibernateGenericDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class HibernatePlaylistDao
    extends HibernateGenericDao<Playlist> implements PlaylistDao
{
    private static final Log log = LogFactory.getLog( HibernatePlaylistDao.class );
    
    @Override
    protected Class<Playlist> getClazz()
    {
        return Playlist.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Playlist> getLikeName( String name )
    {
        return getCriteria( Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase() ).list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Playlist> getByOwner( User owner )
    {
        return getCriteria( Restrictions.eq( "owner", owner ) ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Playlist> getByOwner( User owner, int maxResults )
    {
        return getCriteria( 
            Restrictions.eq( "owner", owner ) ).
            addOrder( Order.desc( "lastUpdated" ) ).
            setMaxResults( maxResults ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Playlist> getByOwnerLikeName( User owner, String name, int maxResults )
    {
        return getCriteria( 
            Restrictions.eq( "owner", owner ), 
            Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase() ).
            addOrder( Order.desc( "lastUpdated" ) ).
            setMaxResults( maxResults ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Playlist> getSharedWith( User user )
    {
        String sql = "select pl.* from playlist pl where exists( " +
            "select u.username from user u, playlist_user pu where u.id = pu.users_id and pu.playlist_id = pl.id and u.username = '" + user.getUsername() + "' )";
        
        log.debug( sql );
        
        return sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( "playlist", Playlist.class ).list();
    }
}
