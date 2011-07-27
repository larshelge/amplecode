package no.tfs.nf.support.deletion;

import no.tfs.nf.api.Category;
import no.tfs.nf.api.Clip;
import no.tfs.nf.api.Document;
import no.tfs.nf.api.Event;
import no.tfs.nf.api.Person;
import no.tfs.nf.api.Playlist;
import no.tfs.nf.api.Team;
import no.tfs.nf.api.User;

public abstract class DeletionHandler
{
    protected abstract Class<?> getTargetClass();
    
    public void deleteCategory( Category category )
    {
    }
    
    public void deleteClip( Clip clip )
    {
    }
    
    public void deleteDocument( Document document )
    {
    }
    
    public void deleteEvent( Event event )
    {
    }
    
    public void deletePerson( Person person )
    {
    }
    
    public void deletePlaylist( Playlist playlist )
    {
    }
    
    public void deleteTeam( Team team )
    {
    }
    
    public void deleteUser( User user )
    {        
    }
    
    public boolean allowDeleteCategory( Category category )
    {
        return true;
    }
    
    public boolean allowDeleteClip( Clip clip )
    {
        return true;
    }
    
    public boolean allowDeleteDocument( Document document )
    {
        return true;
    }
    
    public boolean allowDeleteEvent( Event event )
    {
        return true;
    }
    
    public boolean allowDeletePerson( Person person )
    {
        return true;
    }
    
    public boolean allowDeletePlaylist( Playlist playlist )
    {
        return true;
    }
    
    public boolean allowDeleteTeam( Team team )
    {
        return true;
    }
    
    public boolean allowDeleteUser( User user )
    {
        return true;
    }
}
