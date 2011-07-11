package no.tfs.nf.api;

import java.util.List;

public interface DocumentService
    extends GenericService<Document>
{
    List<Document> get( String q, User user );
}
