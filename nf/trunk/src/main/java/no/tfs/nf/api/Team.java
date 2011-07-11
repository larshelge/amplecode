package no.tfs.nf.api;

import javax.persistence.Entity;

import no.tfs.nf.svx.ObjectFactory;
import no.tfs.nf.svx.XTeam;

@Entity
public class Team
    extends Tag
{
    public Team()
    {
    }
    
    public Team( String code, String name )
    {
        this.code = code;
        this.name = name;
    }
    
    public XTeam toX()
    {
        XTeam team = new ObjectFactory().createXTeam();
        team.setCode( code );
        team.setName( name );
        return team;
    }
}
