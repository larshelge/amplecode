package no.tfs.nf.dao;

import no.tfs.nf.api.User;
import no.tfs.nf.api.UserDao;
import no.tfs.nf.util.HibernateGenericDao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUserDao
    extends HibernateGenericDao<User> implements UserDao
{
    @Override
    protected Class<User> getClazz()
    {
        return User.class;
    }

    @Override
    public User getByUsername( String username )
    {
        return (User) getCriteria( Restrictions.eq( "username", username ) ).uniqueResult();
    }
}
