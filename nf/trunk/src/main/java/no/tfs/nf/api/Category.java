package no.tfs.nf.api;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import no.tfs.nf.svx.ObjectFactory;
import no.tfs.nf.svx.XCategory;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.ForeignKey;

@Entity
public class Category
    extends Tag implements TreeNode
{
    private boolean standard;
    
    private boolean root;
    
    private boolean marker;
    
    @ManyToOne
    @ForeignKey(name="fk_category_parent")
    @JoinColumn(name="parentid")
    private Category parent;
    
    @OneToMany(mappedBy="parent",cascade={CascadeType.ALL})
    private Set<Category> children = new HashSet<Category>();
    
    public Category()
    {   
    }
    
    public Category( String code, String name, boolean standard )
    {
        this.code = code;
        this.name = name;
        this.standard = standard;
    }

    @JsonIgnore
    public Set<Integer> getCategoryWithChildrenIds()
    {
        Set<Integer> ids = new HashSet<Integer>();
        
        for ( Category category : getCategoryWithChildren() )
        {
            ids.add( category.getId() );
        }
        
        return ids;
    }

    @JsonIgnore
    public Set<Category> getCategoryWithChildren()
    {
        Set<Category> children = new HashSet<Category>();
        
        addChildren( this, children );
        
        return children;
    }
    
    private void addChildren( Category category, Set<Category> children )
    {
        children.add( category );
        
        for ( Category child : category.getChildren() )
        {
            addChildren( child, children );
        }
    }    
    
    public boolean hasChildren()
    {
        return children != null && children.size() > 0;
    }
    
    public XCategory toX()
    {
        XCategory category = new ObjectFactory().createXCategory();
        category.setCode( code );
        category.setName( name );
        return category;
    }
    
    public boolean isStandard()
    {
        return standard;
    }

    public void setStandard( boolean standard )
    {
        this.standard = standard;
    }

    public boolean isRoot()
    {
        return root;
    }

    public void setRoot( boolean root )
    {
        this.root = root;
    }

    @JsonIgnore
    public Category getParent()
    {
        return parent;
    }

    public void setParent( Category parent )
    {
        this.parent = parent;
    }

    @JsonIgnore
    public Set<Category> getChildren()
    {
        return children;
    }

    public void setChildren( Set<Category> children )
    {
        this.children = children;
    }

    public boolean isMarker()
    {
        return marker;
    }

    public void setMarker( boolean marker )
    {
        this.marker = marker;
    }
}
