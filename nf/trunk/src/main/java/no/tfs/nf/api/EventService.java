package no.tfs.nf.api;

import java.util.Collection;

public interface EventService
    extends GenericService<Event>
{
    Collection<Event> getLikeName( String name );
}
