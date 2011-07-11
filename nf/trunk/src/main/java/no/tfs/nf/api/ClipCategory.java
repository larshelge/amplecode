package no.tfs.nf.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@Entity
public class ClipCategory
{
    public static final String ID_SEPARATOR = "-";
    public static final String NAME_SEPARATOR = " - ";
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @ManyToOne
    @ForeignKey(name="fk_clipcategory_category")
    private Category category;

    @Column(nullable=false)
    private int grade;

    public ClipCategory()
    {
    }
    
    public ClipCategory( Category category )
    {
        this.category = category;
        this.grade = 0;
    }
    
    public ClipCategory( Category category, int grade )
    {
        this.category = category;
        this.grade = grade;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( category == null ) ? 0 : category.hashCode() );
        result = prime * result + grade;
        
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
        
        ClipCategory other = (ClipCategory) object;
        
        return category.equals( other.category ) && grade == other.grade;
    }
    
    public String getIdentifier()
    {
        return category.getId() + ID_SEPARATOR + grade;
    }
    
    public String getDescription()
    {
        return category.getName() + NAME_SEPARATOR + grade;
    }
    
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory( Category category )
    {
        this.category = category;
    }

    public int getGrade()
    {
        return grade;
    }

    public void setGrade( int grade )
    {
        this.grade = grade;
    }
}
