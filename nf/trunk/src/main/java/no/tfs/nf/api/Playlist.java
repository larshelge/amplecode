package no.tfs.nf.api;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import org.hibernate.annotations.Type;

@Entity
public class Playlist
    extends Tag
{
    @Type(type="text")
    private String description;

    @ManyToOne
    @ForeignKey(name="fk_playlist_user")
    private User owner;
    
    @ManyToMany
    private Set<User> users = new HashSet<User>();

    private Date lastUpdated;
    
    public Playlist()
    {
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner( User owner )
    {
        this.owner = owner;
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers( Set<User> users )
    {
        this.users = users;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }
}
