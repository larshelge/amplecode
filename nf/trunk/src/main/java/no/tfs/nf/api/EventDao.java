package no.tfs.nf.api;

import java.util.Collection;

public interface EventDao
    extends GenericDao<Event>
{
    Collection<Event> getLikeName( String name );
}
