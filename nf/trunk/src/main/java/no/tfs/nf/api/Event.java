package no.tfs.nf.api;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import no.tfs.nf.svx.XEvent;
import no.tfs.nf.util.DateUtils;

import org.hibernate.annotations.ForeignKey;

@Entity
public class Event
    extends Tag
{
    private String location;
    
    private Date date;

    @ManyToOne
    @ForeignKey(name="fk_event_hometeam_team")
    private Team homeTeam;

    @ManyToOne
    @ForeignKey(name="fk_event_awayteam_team")
    private Team awayTeam;

    public Event()
    {
    }
    
    public Event( String code, String name, String location, Date date )
    {
        this.code = code;
        this.name = name;
        this.location = location;
        this.date = date;
    }
    
    public Event fromX( XEvent e )
    {
        setCode( e.getCode() );
        setLocation( e.getLocation() );
        setDate( e.getDate().toGregorianCalendar().getTime() );
        return this;
    }
    
    public void setLogicalName()
    {
        if ( homeTeam != null && awayTeam != null )
        {
            this.name = homeTeam.getName() + " - " + awayTeam.getName() + " (" + DateUtils.getLongDateString( date ) + ")";
        }
    }
    
    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
    {
        this.location = location;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate( Date date )
    {
        this.date = date;
    }

    public Team getHomeTeam()
    {
        return homeTeam;
    }

    public void setHomeTeam( Team homeTeam )
    {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam()
    {
        return awayTeam;
    }

    public void setAwayTeam( Team awayTeam )
    {
        this.awayTeam = awayTeam;
    }
}
