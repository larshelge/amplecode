package org.amplecode.quick.statementbuilder;

import org.amplecode.quick.StatementBuilder;
import org.junit.Test;

import static junit.framework.Assert.*;

public class StatementBuilderTest
{
    @Test
    public void test()
    {
        StatementBuilder builder = new PostgreSqlStatementBuilder();
        
        builder.setTableName( "element" );
        builder.setAutoIncrementColumn( "id" );
        builder.setIdentifierColumn( "id" );
        
        builder.setUniqueColumn( "name" );
        builder.setUniqueValue( "johns' element" );
        
        builder.setColumn( "name" );
        builder.setColumn( "type" );        
        builder.setValue( "johns' element" );
        builder.setValue( "hard /ware" );

        assertEquals( "SELECT id FROM element WHERE ", builder.getIdentifierStatement() );
        assertEquals( "INSERT INTO element (id,name,type) VALUES ", builder.getInsertStatementOpening() );
        assertEquals( "(nextval('hibernate_sequence'),'johns'' element','hard /ware'),", builder.getInsertStatementValues() );
        assertEquals( "SELECT name FROM element WHERE name='johns'' element'", builder.getUniquenessStatement( true ) );
    }
}
