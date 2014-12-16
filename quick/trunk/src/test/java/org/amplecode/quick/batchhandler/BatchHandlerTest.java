package org.amplecode.quick.batchhandler;

import org.amplecode.quick.BatchHandler;
import org.amplecode.quick.model.DataValue;
import org.amplecode.quick.model.DataValueBatchHandler;
import org.amplecode.quick.statement.JdbcStatementManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Lars Helge Overland
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/META-INF/beans.xml"})
public class BatchHandlerTest
{
    @Test
    public void testAdd()
    {
        DataValue v1 = new DataValue( 1, 1, 1, "a" );
        DataValue v2 = new DataValue( 1, 2, 2, "b" );
        DataValue v3 = new DataValue( 1, 3, 3, "c" );
        DataValue v4 = new DataValue( 1, 3, 3, "d" ); // Duplicate
        
        BatchHandler<DataValue> batchHandler = new DataValueBatchHandler( JdbcStatementManager.IN_MEMORY_JDBC_CONFIG ).init();
        
        assertTrue( batchHandler.addObject( v1 ) );
        assertTrue( batchHandler.addObject( v2 ) );
        assertTrue( batchHandler.addObject( v3 ) );
        assertFalse( batchHandler.addObject( v4 ) );
    }
}
