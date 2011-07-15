package no.tfs.nf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryDao;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.api.ClipCategory;
import no.tfs.nf.api.GenericDao;
import no.tfs.nf.api.Statistics;
import no.tfs.nf.util.DefaultGenericService;

@Service
@Transactional
public class DefaultCategoryService
    extends DefaultGenericService<Category> implements CategoryService
{
    @Autowired
    private CategoryDao categoryDao;
    
    @Override
    public GenericDao<Category> getGenericDao()
    {
        return categoryDao;
    }
    
    @Override
    public Category getByCode( String code )
    {
        return categoryDao.getByCode( code );
    }
    
    @Override
    public Collection<Category> getStandard()
    {
        return categoryDao.getStandard();
    }
    
    @Override
    public Collection<Category> getLikeName( String name )
    {
        return categoryDao.getLikeName( name );
    }
    
    @Override
    public Collection<Category> getRootCategories()
    {
        return categoryDao.getRootCategories();
    }

    @Override
    public List<ClipCategory> getClipCategoriesLikeName( String name )
    {
        Collection<Category> categories = getLikeName( name );
        
        List<ClipCategory> clipCategories = new ArrayList<ClipCategory>();
        
        for ( Category category : categories )
        {
            for ( int i = 0; i < 3; i++ )
            {
                int grade = i + 1;
                
                clipCategories.add( new ClipCategory( category, grade ) );
            }
        }
        
        return clipCategories;
    }

    @Override
    public Statistics getStatistics()
    {
        return categoryDao.getStatistics();
    }
}
