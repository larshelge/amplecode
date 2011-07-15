package no.tfs.nf.api;

import java.util.Collection;
import java.util.List;

public interface CategoryService
    extends GenericService<Category>
{
    Category getByCode( String code );
    
    Collection<Category> getStandard();
    
    Collection<Category> getLikeName( String name );
    
    Collection<Category> getRootCategories();
    
    List<ClipCategory> getClipCategoriesLikeName( String name );
    
    Statistics getStatistics();
}
