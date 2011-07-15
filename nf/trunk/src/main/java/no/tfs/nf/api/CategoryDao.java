package no.tfs.nf.api;

import java.util.Collection;

public interface CategoryDao
    extends GenericDao<Category>
{
    Category getByCode( String code );
    
    Collection<Category> getStandard();
    
    Collection<Category> getLikeName( String name );
    
    Collection<Category> getRootCategories();
    
    Statistics getStatistics();
}
