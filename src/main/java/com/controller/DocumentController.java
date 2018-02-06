package com.controller;

import com.model.Document;
import com.service.document.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "documents")
public class DocumentController {

   @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody List<Document> getDocumentsInJSON() {
        return documentService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Object getDocumentByIdInJSON(@PathVariable("id") Long id) throws IOException {
        return documentService.findById(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody Object addDocumentInJSON(@RequestParam CommonsMultipartFile file) {
        return documentService.addDocument(file.getBytes(), file.getFileItem().getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteDocumentInJSON(@PathVariable("id") Long id) {
        return documentService.deleteDocument(id);
    }

    /*@RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody Object writeToFile(@RequestParam String name) throws IOException {
        return documentService.writeToFile(name);
    }*/
}
