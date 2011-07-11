package no.tfs.nf.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import no.tfs.nf.api.User;

public class EmailUtils
{
    private static final String HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "toppfotballsenter";
    private static final String PASSWORD = "fotballab";
    private static final String FROM_ADDRESS = "noreply@toppfotballsenter.no";
    
    public static void sendResetPasswordEmail( User user, String rawRestoreCode )
    {
        String message =
            "Kjære " + user.getFirstname() + " " + user.getLastname() + "\n\n" +
            "Du har bedt om å gjenopprette din bruker-konto på fotballab.no. For å bekrefte dette vennligst følg lenken under. " +
            "Du vil deretter bli bedt om å lage et nytt passord.\n\n" +
            "http://fotballab.no/userRestore?username=" + user.getUsername() + "&restoreCode=" + rawRestoreCode + "\n\n" +
            "Med vennlig hilsen\n\n" +
            "Fotballab.no";
            
        try
        {
            Email email = getEmail();
            email.setSubject( "Gjenopprettelse av din bruker-konto på fotballab.no" );
            email.setMsg( message );
            email.addTo( user.getUsername() );
            email.send();
        }
        catch ( EmailException ex )
        {
            throw new RuntimeException( ex );
        }
    }
    
    private static Email getEmail()
        throws EmailException
    {
        Email email = new SimpleEmail();
        email.setHostName( HOST_NAME );
        email.setSmtpPort( SMTP_PORT );
        email.setAuthenticator( new DefaultAuthenticator( USERNAME, PASSWORD ) );
        email.setTLS( true );
        email.setFrom( FROM_ADDRESS );
        
        return email;
    }
}
