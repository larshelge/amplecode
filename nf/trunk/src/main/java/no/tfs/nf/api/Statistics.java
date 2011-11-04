package no.tfs.nf.api;

import java.util.ArrayList;
import java.util.List;

public class Statistics
{
    private List<Statistics.Record> categoryClipCount = new ArrayList<Statistics.Record>();

    private List<Statistics.Record> viewCount = new ArrayList<Statistics.Record>();

    public List<Statistics.Record> getCategoryClipCount()
    {
        return categoryClipCount;
    }

    public void setCategoryClipCount( List<Statistics.Record> categoryClipCount )
    {
        this.categoryClipCount = categoryClipCount;
    }

    public List<Statistics.Record> getViewCount()
    {
        return viewCount;
    }

    public void setViewCount( List<Statistics.Record> viewCount )
    {
        this.viewCount = viewCount;
    }

    public static class Record
    {
        private String name;
        
        private int value;

        public Record( String name, int value )
        {
            this.name = name;
            this.value = value;
        }
        
        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        public int getValue()
        {
            return value;
        }

        public void setValue( int value )
        {
            this.value = value;
        }
    }
}
