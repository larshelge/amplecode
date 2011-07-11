package no.tfs.nf.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

@Entity
public class Document
{
    private static final String NAME_SUFFIX = "..";
    private static final int TITLE_LENGTH = 60;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(unique=true,nullable=false)
    private String code;

    @Column(nullable=false)
    private String path;

    @Column(nullable=false)
    private String title;
    
    @Type(type="text")
    private String description;
    
    private String requiredAuthority;
    
    @ManyToMany
    private Collection<Category> categories = new HashSet<Category>();

    private Date created;
    
    public Document()
    {
    }
    
    public Document( String path, String title )
    {
        this.path = path;
        this.title = title;
    }

    public Document( String code, String path, String title )
    {
        this.code = code;
        this.path = path;
        this.title = title;
    }

    public String getCategoryName()
    {
        Collection<String> names = new ArrayList<String>();
        
        for ( Category category : categories )
        {
            names.add( category.getName() );
        }
        
        return StringUtils.substring( StringUtils.join( names, ", " ), 0, 37 );
    }
    
    public String getDisplayTitle()
    {
        return title.length() > TITLE_LENGTH ? StringUtils.substring( title, 0, TITLE_LENGTH - NAME_SUFFIX.length() ) + NAME_SUFFIX : title;
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getRequiredAuthority()
    {
        return requiredAuthority;
    }

    public void setRequiredAuthority( String requiredAuthority )
    {
        this.requiredAuthority = requiredAuthority;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( path == null) ? 0 : path.hashCode() );
        
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
        
        Document other = (Document) object;
        
        return path.equals( other.path );
    }

    public String getPath()
    {
        return path;
    }

    public void setPath( String path )
    {
        this.path = path;
    }

    public Collection<Category> getCategories()
    {
        return categories;
    }

    public void setCategories( Collection<Category> categories )
    {
        this.categories = categories;
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
