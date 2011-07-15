package no.tfs.nf.web;

import no.tfs.nf.api.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StatisticsController
{
    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping("/statistics")
    @Secured({"ROLE_MANAGER"})
    public ModelAndView getStatistics()
    {
        return new ModelAndView( "maintenance/statistics" ).addObject( "statistics", categoryService.getStatistics() );
    }
}
