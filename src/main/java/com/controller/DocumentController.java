package com.controller;

import com.model.Agent;
import com.model.Document;
import com.model.Product;
import com.model.RequestWrapper;
import com.service.document.DocumentService;
import com.errors.Error;
import com.validator.DocumentValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @Autowired DocumentValidator documentValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get the list of documents", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of documents are empty", response = Error.class)})
    public @ResponseBody Set<Document> getDocumentsInJSON(Principal principal)
    {
        return documentService.getAllDocuments(principal.getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response", response = Document.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class)
    })
    public @ResponseBody Object getDocumentByIdInJSON(Principal principal, @PathVariable("id") Long id) throws IOException {
        return documentService.getDocumentById(principal.getName(), id);
    }

    @RequestMapping(value = "/tn", method = RequestMethod.POST)
    @ApiOperation(value = "Write to file", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)
    })
    public @ResponseBody Object writeToFile(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) throws IOException {
        documentValidator.validate(requestWrapper, bindingResult);
        return documentService.addDocumentTN(principal.getName(), requestWrapper.getAgent_id(), requestWrapper.getProducts());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class),

    })
    public @ResponseBody Object deleteDocumentInJSON(Principal principal, @PathVariable("id") Long id) {
        return documentService.deleteDocument(principal.getName(), id);
    }
}
