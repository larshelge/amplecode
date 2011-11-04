package no.tfs.nf.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryDao;
import no.tfs.nf.api.Statistics;
import no.tfs.nf.util.HibernateGenericDao;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateCategoryDao
    extends HibernateGenericDao<Category> implements CategoryDao
{
    @Override
    protected Class<Category> getClazz()
    {
        return Category.class;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Category> getStandard()
    {
        return getCriteria( Restrictions.eq( "standard", true ), Restrictions.eq( "marker", false ) ).list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Category> getLikeName( String name )
    {
        return getCriteria( Restrictions.like( "name", name, MatchMode.ANYWHERE ).ignoreCase(), Restrictions.eq( "marker", false ) ).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Category> getRootCategories()
    {
        return getCriteria( Restrictions.eq( "root", true ) ).list();
    }

    @Override
    public Statistics getStatistics()
    {
        final Statistics stats = new Statistics();
        
        String sql = 
            "select distinct ca.name as categoryname, (" +
            "select count(category_id) from clipcategory where category_id=ca.id ) as clipcount " +
            "from category ca, clip order by clipcount desc;";
        
        jdbcTemplate.query( sql, new RowCallbackHandler()
        {          
            @Override
            public void processRow( ResultSet rs ) throws SQLException
            {
                stats.getCategoryClipCount().add( new Statistics.Record( rs.getString( "categoryname" ), rs.getInt( "clipcount" ) ) );                
            }
        } );
        
        sql = 
            "select cl.code, cl.views from clip cl " +
            "where cl.views > 0 " +
            "order by cl.views desc limit 100";
        
        jdbcTemplate.query( sql, new RowCallbackHandler()
        {
            @Override
            public void processRow( ResultSet rs ) throws SQLException
            {
                stats.getViewCount().add( new Statistics.Record( rs.getString( "code" ), rs.getInt( "views" ) ) );
            }
        } );
        
        return stats;
    }
}
