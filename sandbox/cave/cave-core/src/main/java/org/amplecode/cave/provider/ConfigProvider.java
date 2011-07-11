package org.amplecode.cave.provider;

import org.amplecode.cave.CaveConfiguration;

/**
 * Basic interface for a configuration provider. It needs a reference to the
 * ConfigurationManager, and the providing can be controlled by the provide
 * method.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: ConfigProvider.java 45 2007-12-19 15:36:52Z torgeilo $
 */
public interface ConfigProvider
{
    void setConfiguration( CaveConfiguration configuration );

    void provide()
        throws Exception;
}
