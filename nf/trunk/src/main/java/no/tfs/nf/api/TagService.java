package no.tfs.nf.api;

import java.util.Collection;

public interface TagService
{
    Collection<Tag> getCategoryTeamPersonLikeName( String name );
    
    Collection<Tag> getEventTeamLikeName( String name );
}
