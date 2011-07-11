package no.tfs.nf.web;

import no.tfs.nf.api.ExportService;
import no.tfs.nf.svx.Svx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class TestController
{
    private static final String TEST_URL = "http://localhost:8080/nf/importStream";

    private static final Log log = LogFactory.getLog( TestController.class );
    
    @Autowired
    private ExportService exportService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @RequestMapping("/metaTest")
    @Secured({"ROLE_ADMIN"})
    public String metaTest()
    {
        Svx svx = exportService.getMeta();

        log.info( "Request: " + svx );
        
        String response = restTemplate.postForObject( TEST_URL, svx, String.class );
        
        log.info( "Response: " + response );
        
        return "forward:import";
    }
}
