package no.tfs.nf.interceptor;

import static no.tfs.nf.util.UrlUtils.PARAM_AUTHORITIES;
import static no.tfs.nf.util.UrlUtils.PARAM_BASE_URL;
import static no.tfs.nf.util.UrlUtils.PARAM_CURRENT_USER;
import static no.tfs.nf.util.UrlUtils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.tfs.nf.api.User;
import no.tfs.nf.api.UserService;
import no.tfs.nf.util.Encoder;
import no.tfs.nf.util.UrlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class StaticReferenceInterceptor
    extends HandlerInterceptorAdapter
{
    private static final Encoder ENCODER = new Encoder();
    
    @Autowired
    private UserService userService;
    
    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mav )
        throws Exception
    {
        if ( mav != null )
        {
            mav.addObject( PARAM_STATIC_BASE_URL, UrlUtils.staticBaseUrl() );
            mav.addObject( PARAM_STATIC_LOCATION, UrlUtils.staticLocation() );
            mav.addObject( PARAM_BASE_URL, UrlUtils.baseUrl( request ) );
            mav.addObject( PARAM_AUTHORITIES, User.AUTHORITIES );
            mav.addObject( PARAM_CURRENT_USER, userService.getCurrentUser() );
            mav.addObject( "encode", ENCODER );
        }
    }
}
