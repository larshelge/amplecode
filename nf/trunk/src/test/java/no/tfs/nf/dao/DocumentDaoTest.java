package no.tfs.nf.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Collection;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.Document;
import no.tfs.nf.api.DocumentDao;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class DocumentDaoTest
{
    @Autowired
    private DocumentDao documentDao;
    
    @Autowired
    private CategoryService categoryService;
    
    @Test
    public void saveGet()
    {
        Category categoryA = new Category( "categorya", "CategoryA", true );
        Category categoryB = new Category( "categoryb", "CategoryB", true );
        
        categoryService.save( categoryA );
        categoryService.save( categoryB );
        
        Document documentA = new Document( "dA", "PathA", "DocumentA" );
        documentA.getCategories().add( categoryA );
        documentA.getCategories().add( categoryB );
        
        int id = documentDao.save( documentA );
        
        assertEquals( documentA, documentDao.get( id ) );
        assertEquals( documentA.getCategories().size(), documentDao.get( id ).getCategories().size() );
    }

    @Test
    @Ignore //TODO transaction not committed?
    public void getBySubjectName()
    {
        Category categoryA = new Category( "categorya", "CategoryA", true );
        Category categoryB = new Category( "categoryb", "CategoryB", true );
        
        categoryService.save( categoryA );
        categoryService.save( categoryB );
        
        Document documentA = new Document( "dA", "PathA", "DocumentA" );
        documentA.getCategories().add( categoryA );

        Document documentB = new Document( "dB", "PathB", "DocumentB" );
        documentB.getCategories().add( categoryB );
        
        documentDao.save( documentA );
        documentDao.save( documentB );
        
        Collection<Document> documents = documentDao.get( "SubjectA", null );
        
        assertNotNull( documents );
        assertEquals( 1, documents.size() );
    }
}
