package no.tfs.nf.api;

public interface UserDao
    extends GenericDao<User>
{
    User getByUsername( String username );
}
