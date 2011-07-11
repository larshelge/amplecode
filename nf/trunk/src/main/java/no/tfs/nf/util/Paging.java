package no.tfs.nf.util;

public class Paging
{
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    private static final int PAGE_OFFSET = 3; // Each side of current page
    private static final int PAGE_TOTAL_OFFSET = PAGE_OFFSET * 2; // Both sides of current page

    private int currentPage;

    private int total;

    private int pageSize;
    
    public Paging()
    {
        currentPage = 1;
        total = 0;
    }
    
    public Paging( int currentPage, int total )
    {
        this.currentPage = currentPage;
        this.total = total;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Paging( int currentPage, int total, int pageSize )
    {
        this.currentPage = currentPage;
        this.total = total;
        this.pageSize = pageSize;
    }

    public int getNumberOfPages()
    {
        return  total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    /**
     * Returns first page number in paging range.
     */
    public int getStartPage()
    {
        int startPage = 1;
        
        if ( currentPage > PAGE_OFFSET ) // Far enough from start, set start page
        {
            startPage = currentPage - PAGE_OFFSET;
        }
        
        if ( ( getNumberOfPages() - startPage ) < PAGE_TOTAL_OFFSET ) // Too close to end, decrease start page to maintain page range length
        {
            startPage = getNumberOfPages() - PAGE_TOTAL_OFFSET;            
        }

        if ( startPage <= 0 ) // Cannnot be 0 or less, set start page to 1
        {
            startPage = 1;
        }
        
        return startPage;
    }

    /**
     * Returns the last page number in paging range.
     */
    public int getEndPage()
    {
        int endPage = getStartPage() + PAGE_TOTAL_OFFSET;
        
        if ( endPage > getNumberOfPages() ) // Too few pages
        {
            endPage = getNumberOfPages();
        }
        
        return endPage;
    }
    
    public int getCurrentPage()
    {
        if ( currentPage > getNumberOfPages() )
        {
            currentPage = getNumberOfPages();
        }
        
        return currentPage;
    }

    public void setCurrentPage( int currentPage )
    {
        this.currentPage = currentPage > 0 ? currentPage : 1;
    }
    
    public boolean isPrevious()
    {
        return getCurrentPage() > 1;
    }

    public int getPreviousPage()
    {
        return isPrevious() ? getCurrentPage() - 1 : getCurrentPage();
    }
    
    public boolean isNext()
    {
        return getCurrentPage() < getNumberOfPages();
    }
    
    public int getNextPage()
    {
        return isNext() ? getCurrentPage() + 1 : getCurrentPage();
    }
    
    public int getTotal()
    {
        return total;
    }

    public void setTotal( int total )
    {
        this.total = total;
    }
    
    /**
     * Returns first row number in paged table for current page.
     */
    public int getStartPos()
    {
        int startPos = currentPage <= 0 ? 0 : ( currentPage - 1 ) * pageSize;
        startPos = ( startPos >  total ) ? total : startPos;
        return startPos;
    }

    /**
     * Returns last row number in paged table for current page.
     */
    public int getEndPos()
    {
        int endPos = getStartPos() + pageSize;
        endPos = ( endPos > total ) ? total : endPos; 
        return endPos;
    }
}
