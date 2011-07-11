package no.tfs.nf.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionUtilsWrapper
{
    public static <T> List<T> sort( Collection<T> collection, Comparator<T> comparator )
    {
        List<T> list = new ArrayList<T>( collection );
        Collections.sort( list, comparator );
        return list;
    }
}
