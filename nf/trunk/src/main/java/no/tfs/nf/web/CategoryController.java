package no.tfs.nf.web;

import static no.tfs.nf.util.CollectionUtilsWrapper.sort;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.CategoryService;
import no.tfs.nf.comparator.TagNameComparator;
import no.tfs.nf.util.TreeParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping("/categoryTree")
    public @ResponseBody String getRootCategory()
    {
        StringBuilder builder = new StringBuilder();
        
        for ( Category root : categoryService.getRootCategories() )
        {
            builder.append( TreeParser.parse( root ) );
        }
        
        return builder.toString();
    }

    @RequestMapping("/listCategories")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView listCategories()
    {
        return new ModelAndView( "maintenance/categories" ).addObject( "categories", sort( categoryService.getAll(), new TagNameComparator<Category>() ) );
    }
    
    @RequestMapping("/category")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView getCategory( @RequestParam(value="id",required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/category" );
        
        if ( id != null )
        {
            mav.addObject( "category", categoryService.get( id ) );
        }
        
        return mav;
    }
    
    @RequestMapping("/saveCategory")
    @Secured({"ROLE_MANAGER"})
    public String saveCategory( @ModelAttribute("category") Category category, BindingResult result )
    {
        if ( category.getId() > 0 )
        {
            categoryService.update( category );
        }
        else
        {
            categoryService.save( category );
        }
        
        return "forward:listCategories";
    }
    
    @RequestMapping("/deleteCategory")
    @Secured({"ROLE_MANAGER"})
    public String deleteCategory( @RequestParam Integer id )
    {
        categoryService.delete( categoryService.get( id ) );
        
        return "forward:listCategories";
    }

    @RequestMapping("/categoryCodeAvailable")
    public @ResponseBody Boolean codeAvailable( @RequestParam String code, @RequestParam(value="id",required=false) Integer id )
    {
        if ( id != null ) // Update
        {
            Category category = categoryService.get( id );
            
            if ( category != null && category.getCode().trim().equals( code.trim() ) )
            {
                return true;
            }
        }
        
        return categoryService.getByCode( code ) == null;
    }
}
