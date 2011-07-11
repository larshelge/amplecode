package no.tfs.nf.dao;

import java.util.Collection;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryDao;
import no.tfs.nf.util.HibernateGenericDao;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
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
}
