package no.tfs.nf.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import no.tfs.nf.svx.Svx;

public interface ImportService
{
    void importSvx( Svx svx )
        throws IOException;
    
    void importFile( ZipFile file )
        throws IOException;
    
    Svx unmarshal( InputStream in );
}
