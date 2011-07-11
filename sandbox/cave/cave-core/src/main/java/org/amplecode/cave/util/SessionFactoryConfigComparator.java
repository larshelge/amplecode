package org.amplecode.cave.util;

import java.util.Comparator;

import org.amplecode.cave.SessionFactoryConfig;

/**
 * Simple comparator for {@link SessionFactoryConfig}s which compares the names
 * of the configurations.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SessionFactoryConfigComparator.java 43 2007-10-28 22:36:39Z torgeilo $
 */
public class SessionFactoryConfigComparator
    implements Comparator<SessionFactoryConfig>
{
    public final int compare( SessionFactoryConfig config0, SessionFactoryConfig config1 )
    {
        return config0.getName().compareTo( config1.getName() );
    }
}
