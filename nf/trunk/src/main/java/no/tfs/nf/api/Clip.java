package no.tfs.nf.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import no.tfs.nf.svx.XClip;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

@Entity
public class Clip
{
    private static final String NAME_SUFFIX = "..";
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Column(unique=true,nullable=false)
    private String code;
    
    private int start;
    
    @Column(nullable=false)
    private String filename;
    
    @Cascade(value={CascadeType.ALL})
    @OneToMany
    @JoinColumn(name="clip_id")
    @ForeignKey(name="fk_clipcategory_clip")
    @JsonIgnore
    private Set<ClipCategory> clipCategories = new HashSet<ClipCategory>();

    @ManyToMany
    private Set<Person> persons = new HashSet<Person>();
    
    @ManyToMany
    @JsonIgnore
    private Set<Playlist> playlists = new HashSet<Playlist>();
    
    @ManyToOne
    @ForeignKey(name="fk_clip_team")
    private Team team;
    
    @ManyToOne
    @ForeignKey(name="fk_clip_event")
    private Event event;

    @Cascade(value={CascadeType.ALL})
    @OneToMany
    @ForeignKey(name="fk_clip_comment")
    private Set<Comment> comments = new HashSet<Comment>();
    
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @JsonIgnore
    private String description;
    
    private Date created;
    
    public Clip()
    {
    }
    
    public Clip( Set<ClipCategory> clipCategories, Set<Person> persons, Set<Playlist> playlists, Team team, Event event, Type type )
    {
        this.clipCategories = clipCategories;
        this.persons = persons;
        this.playlists = playlists;
        this.team = team;
        this.event = event;
        this.type = type;
    }

    public Clip( String code, String filename, Set<ClipCategory> clipCategories, Set<Person> persons, Set<Playlist> playlists, Team team, Event event, Type type )
    {
        this.code = code;
        this.filename = filename;
        this.clipCategories = clipCategories;
        this.persons = persons;
        this.playlists = playlists;
        this.team = team;
        this.event = event;
        this.type = type;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( code == null ) ? 0 : code.hashCode() );
        
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        Clip other = (Clip) object;
        
        return code.equals( other.code );
    }
    
    public Clip fromX( XClip c )
    {
        setStart( c.getStart() );
        setFilename( c.getFilename() );
        return this;
    }
    
    public List<Tag> getTags()
    {
        List<Tag> tags = new ArrayList<Tag>();
        
        tags.addAll( getCategories() );
        tags.addAll( persons );
        tags.add( team );
        tags.add( event );
        
        return tags;
    }

    public int getCategoriesGrade()
    {
        int grade = 0;
        
        for( ClipCategory clipCategory : clipCategories )
        {
            grade += clipCategory.getGrade();
        }
        
        return grade;
    }
    
    public void addCategory( Category category )
    {
        ClipCategory clipCategory = new ClipCategory( category, 1 );
        this.clipCategories.add( clipCategory );
    }
    
    public void addPerson( Person person )
    {
        this.persons.add( person );
    }
    
    /**
     * Do not use this method for persistence purposes.
     */
    @JsonSerialize
    public Set<Category> getCategories()
    {
        Set<Category> categories = new HashSet<Category>();
        
        for ( ClipCategory clipCategory : clipCategories )
        {
            if ( clipCategory.getCategory() != null )
            {
                categories.add( clipCategory.getCategory() );
            }
        }
        
        return categories;
    }

    public String getCategoryName()
    {
        List<String> names = new ArrayList<String>();
        
        Set<Category> categories = getCategories();
        
        for ( Category category : categories )
        {
            names.add( category.getName() );
        }
        
        return categories != null && categories.size() > 0 ? StringUtils.join( names, ", " ) : "-";
    }
    
    public String getCategoryName( int length )
    {
        String cName = getCategoryName();
        
        return cName.length() > length ? StringUtils.substring( cName, 0, length - NAME_SUFFIX.length() ) + NAME_SUFFIX : cName;
    }
    
    public String getPersonName()
    {
        List<String> names = new ArrayList<String>();
        
        for ( Person person : persons )
        {
            names.add( person.getName() );
        }

        return persons != null && persons.size() > 0 ? StringUtils.join( names, ", " ) : "-";
    }
    
    public String getPersonName( int length )
    {
        String pName = getPersonName();
        
        return pName.length() > length ? StringUtils.substring( pName, 0, length - NAME_SUFFIX.length() ) + NAME_SUFFIX : pName;
    }
    
    public boolean removeComment( int id )
    {
        Iterator<Comment> iterator = comments.iterator();
        
        while ( iterator.hasNext() )
        {
            Comment comment = iterator.next();
            
            if ( comment.getId() == id )
            {
                iterator.remove();
                return true;
            }
        }
        
        return false;
    }
        
    public String toString()
    {
        return "[" + code + "]";
    }
    
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart( int start )
    {
        this.start = start;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename( String filename )
    {
        this.filename = filename;
    }

    public Set<ClipCategory> getClipCategories()
    {
        return clipCategories;
    }

    public void setClipCategories( Set<ClipCategory> clipCategories )
    {
        this.clipCategories = clipCategories;
    }

    public Set<Person> getPersons()
    {
        return persons;
    }

    public void setPersons( Set<Person> persons )
    {
        this.persons = persons;
    }

    public Set<Playlist> getPlaylists()
    {
        return playlists;
    }

    public void setPlaylists( Set<Playlist> playlists )
    {
        this.playlists = playlists;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam( Team team )
    {
        this.team = team;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent( Event event )
    {
        this.event = event;
    }

    public Set<Comment> getComments()
    {
        return comments;
    }

    public void setComments( Set<Comment> comments )
    {
        this.comments = comments;
    }

    public Type getType()
    {
        return type;
    }

    public void setType( Type type )
    {
        this.type = type;
    }
    
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }
}
