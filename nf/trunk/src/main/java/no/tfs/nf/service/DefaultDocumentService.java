package no.tfs.nf.service;

import java.util.Date;
import java.util.List;

import no.tfs.nf.api.Document;
import no.tfs.nf.api.DocumentDao;
import no.tfs.nf.api.DocumentService;
import no.tfs.nf.api.User;
import no.tfs.nf.util.DefaultGenericService;
import no.tfs.nf.util.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultDocumentService
    extends DefaultGenericService<Document> implements DocumentService
{
    @Autowired
    private DocumentDao documentDao;
    
    public DocumentDao getGenericDao()
    {
        return documentDao;
    }
    
    @Override
    public int save( Document document )
    {
        document.setCode( document.getCode() == null || document.getCode().trim().isEmpty() ? UuidUtils.getDocumentUuid() : document.getCode() );
        document.setCreated( new Date() );
        
        return documentDao.save( document );
    }
    
    @Override
    public void update( Document document )
    {
        document.setCreated( new Date() );
        
        documentDao.update( document );
    }
    
    public List<Document> get( String q, User user )
    {
        return documentDao.get( q, user );
    }
}
