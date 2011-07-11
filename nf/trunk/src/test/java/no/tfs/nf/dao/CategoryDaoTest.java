package no.tfs.nf.dao;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class CategoryDaoTest
{
    @Autowired
    private CategoryDao categoryDao;
    
    @Test
    public void saveGet()
    {
        Category categoryA = new Category( "categorya", "CategoryA", true );
        Category categoryB = new Category( "categoryb", "CategoryB", true );
        
        int idA = categoryDao.save( categoryA );
        int idB = categoryDao.save( categoryB );
        
        assertEquals( categoryA, categoryDao.get( idA ) );
        assertEquals( categoryB, categoryDao.get( idB ) );        
    }
    
    @Test
    public void parentChild()
    {
        Category categoryA = new Category( "categorya", "CategoryA", true );
        categoryDao.save( categoryA );

        Category categoryB = new Category( "categoryb", "CategoryB", true );
        categoryB.setParent( categoryA );
        int idB = categoryDao.save( categoryB );

        Category categoryC = new Category( "categoryc", "CategoryC", true );
        categoryC.setParent( categoryA );
        int idC = categoryDao.save( categoryC );

        Category categoryD = new Category( "categoryd", "CategoryD", true );
        categoryD.setParent( categoryB );
        int idD = categoryDao.save( categoryD );
        
        assertEquals( categoryA, categoryDao.get( idB ).getParent() );
        assertEquals( categoryA, categoryDao.get( idC ).getParent() );
        assertEquals( categoryB, categoryDao.get( idD ).getParent() );
        assertEquals( categoryA, categoryDao.get( idD ).getParent().getParent() );
    }
}
