package no.tfs.nf.dao;

import java.util.List;

import no.tfs.nf.api.Document;
import no.tfs.nf.api.DocumentDao;
import no.tfs.nf.api.User;
import no.tfs.nf.util.HibernateGenericDao;
import no.tfs.nf.util.TextUtils;
import no.tfs.nf.util.UrlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import static no.tfs.nf.util.TextUtils.*;

@Repository
public class HibernateDocumentDao
    extends HibernateGenericDao<Document> implements DocumentDao
{
    private static final Log log = LogFactory.getLog( HibernateDocumentDao.class );
    
    @Override
    protected Class<Document> getClazz()
    {
        return Document.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Document> get( String q, User user )
    {
        String[] split = q.split( UrlUtils.DELIMITERS );
        
        String sql = "select * from document d";
        
        int first = 0;
        
        if ( user == null ) // Accept null required authority only
        {
            sql += ( first++ == 0 ? " where" : " and" ) + " ( d.requiredauthority is null )";
        }
        else // Accept null or minimum required authority
        {
            sql += ( first++ == 0 ? " where" : " and" ) + " ( d.requiredauthority is null or d.requiredauthority IN ( " + TextUtils.join( user.getAuthorities(), ",", "'" ) + " ) )";
        }
        
        for ( String s : split )
        {
            s = s.trim().toLowerCase();
            
            sql += ( first++ == 0 ? " where" : " and" ) + " exists( " +
                "select c.name from category c, document_category dc where c.id = dc.categories_id and dc.document_id = d.id and lower(c.name) = '" + s + "' )" + 
                " or" + getSimilarToWordClause( "d.title", s );
        }
        
        sql += " order by d.created";
        
        log.debug( sql );
        
        return sessionFactory.getCurrentSession().createSQLQuery( sql ).addEntity( Document.class ).list();
    }
}
