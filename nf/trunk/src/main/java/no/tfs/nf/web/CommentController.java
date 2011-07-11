package no.tfs.nf.web;

import java.util.Date;

import no.tfs.nf.api.Clip;
import no.tfs.nf.api.ClipService;
import no.tfs.nf.api.Comment;
import no.tfs.nf.api.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommentController
{
    @Autowired
    private ClipService clipService;

    @Autowired
    private UserService userService;
    
    @RequestMapping("/comments")
    public ModelAndView getComments( @RequestParam Integer clipId )
    {
        return new ModelAndView( "comments" ).addObject( "clip", clipService.get( clipId ) );
    }
    
    @RequestMapping("/saveComment")
    public @ResponseBody Boolean saveComment( @RequestParam Integer clipId, @RequestParam String body )
    {
        Comment comment = new Comment();
        
        comment.setBody( body );
        comment.setLastUpdated( new Date() );
        comment.setOwner( userService.getCurrentUser() );
        
        Clip clip = clipService.get( clipId );
        clip.getComments().add( comment );
        clipService.update( clip );
        
        return true;
    }
    
    @RequestMapping("/deleteComment")
    public @ResponseBody Boolean deleteComment( @RequestParam Integer clipId, @RequestParam Integer commentId )
    {
        Clip clip = clipService.get( clipId );        
        boolean removed = clip.removeComment( commentId );
        clipService.update( clip );
        return removed;
    }
}
