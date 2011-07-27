package no.tfs.nf.web;

import static no.tfs.nf.util.CollectionUtilsWrapper.sort;

import java.util.Arrays;

import no.tfs.nf.api.User;
import no.tfs.nf.api.UserService;
import no.tfs.nf.comparator.UserNameComparator;

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
public class UserController
{
    @Autowired
    private UserService userService;
    
    @RequestMapping("/user")
    @Secured({"ROLE_ADMIN"})
    public ModelAndView getUser( @RequestParam(value="id",required=false) Integer id )
    {
        ModelAndView mav = new ModelAndView( "maintenance/user" ).addObject( "userroles", Arrays.asList( User.USERROLES ) );;
        
        if ( id != null )
        {
            mav.addObject( "user", userService.get( id ) );
        }
        
        return mav;
    }

    @RequestMapping("/saveUser")
    @Secured({"ROLE_ADMIN"})
    public String saveUser( @ModelAttribute("user") User user, BindingResult result )
    {
        user.setPassword( userService.encodePassword( user.getPassword() ) );
        
        if ( user.getId() > 0 )
        {
            userService.update( user );
        }
        else
        {
            userService.save( user );
        }
        
        return "forward:users";
    }
    
    @RequestMapping("/deleteUser")
    @Secured({"ROLE_ADMIN"})
    public String deleteUser( @RequestParam Integer id )
    {
        userService.delete( userService.get( id ) );
        
        return "forward:users";
    }

    @RequestMapping("/users")
    @Secured({"ROLE_ADMIN"})
    public ModelAndView getAllUsers()
    {
        return new ModelAndView( "maintenance/users" ).addObject( "users", sort( userService.getAll(), new UserNameComparator() ) );
    }

    @RequestMapping("/registerUser")
    public String registerUser( @ModelAttribute("user") User user, BindingResult result )
    {
        user.setPassword( userService.encodePassword( user.getPassword() ) );
        user.setUserrole( User.USERROLES[0] );
        
        if ( user.getId() > 0 )
        {
            userService.update( user );
        }
        else
        {
            userService.save( user );
        }
        
        return "forward:login";
    }
    
    @RequestMapping("/usernameAvailable")
    public @ResponseBody Boolean usernameAvailable( @RequestParam String username )
    {
        return userService.getByUsername( username ) == null;
    }
    
    @RequestMapping("/requestUserRestore")
    public String requestUserRestore( @RequestParam String username )
    {
        userService.requestUserRestore( username );
        
        return "forward:login";
    }
    
    @RequestMapping("/userRestore")
    public ModelAndView userRestore( @RequestParam String username, @RequestParam String restoreCode )
    {
        boolean canRestore = userService.canRestoreUser( username, restoreCode );
        
        if ( canRestore )
        {
            return new ModelAndView( "userRestore" ).addObject( "username", username ).addObject( "restoreCode", restoreCode );
        }
        else
        {
            return new ModelAndView( "login" );
        }
    }
    
    @RequestMapping("/restoreUser")
    public String restoreUser( @RequestParam String username, @RequestParam String restoreCode, @RequestParam String password )
    {
        userService.restoreUser( username, restoreCode, password );
        
        return "forward:login";
    }
}
