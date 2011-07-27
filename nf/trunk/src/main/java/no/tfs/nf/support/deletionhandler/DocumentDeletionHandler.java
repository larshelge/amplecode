package no.tfs.nf.support.deletionhandler;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.Document;
import no.tfs.nf.support.deletion.DeletionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentDeletionHandler
    extends DeletionHandler
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Class<?> getTargetClass()
    {
        return Document.class;
    }

    @Override
    public void deleteCategory( Category category )
    {
        final String sql = "delete from document_category where categories_id=" + category.getId();
        
        jdbcTemplate.execute( sql );
    }
}
