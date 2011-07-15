package no.tfs.nf.api;

import java.util.ArrayList;
import java.util.List;

public class Statistics
{
    private List<String> categoryClipCount = new ArrayList<String>();

    public List<String> getCategoryClipCount()
    {
        return categoryClipCount;
    }

    public void setCategoryClipCount( List<String> categoryClipCount )
    {
        this.categoryClipCount = categoryClipCount;
    }
}
