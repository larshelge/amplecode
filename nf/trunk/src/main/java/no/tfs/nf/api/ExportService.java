package no.tfs.nf.api;

import java.io.IOException;
import java.io.OutputStream;

import no.tfs.nf.svx.Svx;

public interface ExportService
{
    Svx getMeta();
    
    void exportMeta( OutputStream out )
        throws IOException;
}
