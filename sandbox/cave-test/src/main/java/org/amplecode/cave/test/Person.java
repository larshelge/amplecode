package org.amplecode.cave.test;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: Person.java 17 2007-09-28 09:59:29Z torgeilo $
 */
public class Person
{
    private int id;

    private String name;

    private String emailAddress;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Person()
    {
    }

    public Person( String name, String emailAddress )
    {
        this.name = name;
        this.emailAddress = emailAddress;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress( String emailAddress )
    {
        this.emailAddress = emailAddress;
    }
}
