package no.tfs.nf.api;

import java.util.List;

public interface DocumentDao
    extends GenericDao<Document>
{
    List<Document> get( String q, User user );
}
