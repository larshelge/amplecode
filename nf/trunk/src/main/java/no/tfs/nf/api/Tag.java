package no.tfs.nf.api;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

@MappedSuperclass
public abstract class Tag
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected int id;

    @Column(unique=true,nullable=false)
    protected String code;
    
    @Column(nullable=false)
    protected String name;
    
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

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getSubName( int chars )
    {
        return StringUtils.substring( name, 0, chars );
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
        
        Tag other = (Tag) object;
        
        return code.equals( other.code );
    }
    
    @Override
    public String toString()
    {
        return "[" + code + ", " + name + "]";
    }
}
