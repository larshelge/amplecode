package no.tfs.nf.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import no.tfs.nf.api.User;
import no.tfs.nf.api.UserDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
@Transactional
public class UserDaoTest
{
    @Autowired
    private UserDao userDao;
    
    @Test
    public void saveGet()
    {
        User user = new User( "username", "password", "firstname", "lastname", User.USERROLES[0] );
        
        int id = userDao.save( user );
        
        assertNotNull( userDao.get( id ) );
        assertEquals( user, userDao.get( id ) );
    }
    
    @Test
    public void saveGetWithUserRole()
    {
        Set<String> authorities = new HashSet<String>();
        authorities.add( "authority1" );
        authorities.add( "authority2" );
        authorities.add( "authority3" );
        
        User user = new User( "username", "password", "firstname", "lastname", User.USERROLES[0] );
        
        int id = userDao.save( user );
        
        assertNotNull( userDao.get( id ) );
        assertEquals( user, userDao.get( id ) );
    }
}
