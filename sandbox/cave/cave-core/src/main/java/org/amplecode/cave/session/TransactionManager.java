package org.amplecode.cave.session;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: TransactionManager.java 37 2007-10-04 18:51:13Z torgeilo $
 */
public interface TransactionManager
{
    void enter();

    void leave();

    void abort();
}
