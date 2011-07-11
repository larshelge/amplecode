package no.tfs.nf.web;

import no.tfs.nf.api.ExportService;
import no.tfs.nf.svx.Svx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DownloadController
{
    @Autowired
    private ExportService exportService;
    
    @RequestMapping("/meta")
    public @ResponseBody Svx downloadMeta()
    {
        return exportService.getMeta();
    }
}
