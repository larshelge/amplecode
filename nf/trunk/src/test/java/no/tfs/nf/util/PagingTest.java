package no.tfs.nf.util;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static junit.framework.Assert.*;

public class PagingTest
{
    private static final int PAGE_SIZE = 4;
    
    @Test
    public void testPaging()
    {
        List<Integer> list = Arrays.asList( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 );
        
        Paging p = new Paging( 1, list.size(), PAGE_SIZE );
        
        assertEquals( 1, p.getCurrentPage() );
        assertEquals( 6, p.getNumberOfPages() );
        assertEquals( 1, p.getStartPage() );
        assertEquals( 6, p.getEndPage() );
        assertEquals( 0, p.getStartPos() );
        assertEquals( 4, p.getEndPos() );
        assertFalse( p.isPrevious() );
        assertTrue( p.isNext() );
        assertEquals( 2, p.getNextPage() );
        
        p = new Paging( 2, list.size(), PAGE_SIZE );

        assertEquals( 2, p.getCurrentPage() );
        assertEquals( 6, p.getNumberOfPages() );
        assertEquals( 1, p.getStartPage() );
        assertEquals( 6, p.getEndPage() );
        assertEquals( 4, p.getStartPos() );
        assertEquals( 8, p.getEndPos() );
        assertTrue( p.isPrevious() );
        assertEquals( 1, p.getPreviousPage() );
        assertTrue( p.isNext() );
        assertEquals( 3, p.getNextPage() );

        p = new Paging( 6, list.size(), PAGE_SIZE );

        assertEquals( 6, p.getCurrentPage() );
        assertEquals( 6, p.getNumberOfPages() );
        assertEquals( 1, p.getStartPage() );
        assertEquals( 6, p.getEndPage() );
        assertEquals( 20, p.getStartPos() );
        assertEquals( 22, p.getEndPos() );
        assertTrue( p.isPrevious() );
        assertEquals( 5, p.getPreviousPage() );
        assertFalse( p.isNext() );
    }
}
