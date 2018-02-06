package com.controller;

import com.model.Document;
import com.service.document.DocumentService;
import com.utils.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "documents")
@Api(value = "DocumentsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get the list of documents", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of documents are empty", response = Error.class)})
    public @ResponseBody List<Document> getDocumentsInJSON() {
        return documentService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class),

    })
    public @ResponseBody Object getDocumentByIdInJSON(@PathVariable("id") Long id) throws IOException {
        return documentService.findById(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Add a new Document", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class),

    })
    public @ResponseBody Object addDocumentInJSON(@RequestParam CommonsMultipartFile file) {
        return documentService.addDocument(file.getBytes(), file.getFileItem().getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class),

    })
    public @ResponseBody Object deleteDocumentInJSON(@PathVariable("id") Long id) {
        return documentService.deleteDocument(id);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ApiOperation(value = "Write to file", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 404, message = "Name not found", response = Error.class),

    })
    public @ResponseBody Object writeToFile(@RequestParam String name) throws IOException {
        return documentService.writeToFile(name);
    }
}
