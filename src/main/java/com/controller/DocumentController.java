package com.controller;

import com.model.Document;
import com.service.document.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "documents")
public class DocumentController
{
    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody Set<Document> getDocumentsInJSON(Principal principal)
    {
        return documentService.findAll(principal.getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Object getDocumentByIdInJSON(Principal principal, @PathVariable("id") Long id) throws IOException {
        return documentService.findById(principal.getName(), id);
    }

    @RequestMapping(value = "/tn", method = RequestMethod.POST)
    public @ResponseBody Object writeToFile(Principal principal, @RequestParam String name) throws IOException {
        return documentService.writeToFileTN(principal.getName(), name);
    }

    /*@RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody Object addDocumentInJSON(Principal principal, @RequestParam CommonsMultipartFile file) {
        return documentService.addDocument(principal.getName(), file.getBytes(), file.getFileItem().getName());
    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteDocumentInJSON(Principal principal, @PathVariable("id") Long id) {
        return documentService.deleteDocument(principal.getName(), id);
    }
}
