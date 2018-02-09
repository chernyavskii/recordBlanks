package com.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @ApiOperation(value = "Get list of documents", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "List of documents are empty", response = Error.class)})
    public @ResponseBody ResponseEntity<?> getDocumentsInJSON(Principal principal)
    {
        Set<Document> documentList = documentService.getAllDocuments(principal.getName());
        if (documentList.size() == 0) {
            Error error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Set<Document>>(documentList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Document", response = Document.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getDocumentByIdInJSON(Principal principal, @PathVariable("id") Long id) throws IOException {
        Document document = documentService.getDocumentById(principal.getName(), id);
        if(document == null){
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<Document>(document, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/tn", method = RequestMethod.POST)
    @ApiOperation(value = "Write to file", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> writeToFile(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) throws IOException {
/*
        documentValidator.validate(requestWrapper, bindingResult);
*/
        Error error;
        requestWrapper.getProducts().size();
        ////ПРОВЕРИТЬ НА РАЗМЕР СПИСКА
        ////SWITCH/CASE НА ВСЕ СЛУЧАИ В Procuct, ПОТОМУ ЧТО МЫ ПЕРЕдАЕМ В RequestWrapper только 2 поля, а нам надо весь объект Product,
        //поэтому доделай тут вручную, всё тут проверь
        List<Product> list = requestWrapper.getProducts();
        for(Product product : list){
            if(product.getName().equals(""))
            {
                error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(documentService.addDocumentTN(principal.getName(), requestWrapper.getAgent_id(), requestWrapper.getProducts()), HttpStatus.OK) ;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully",response = Document.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class),
    })
    public @ResponseBody ResponseEntity<?> deleteDocumentInJSON(Principal principal, @PathVariable("id") Long id) throws java.io.IOException {
        if (documentService.getDocumentById(principal.getName(),id) == null) {
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Object>(documentService.deleteDocument(principal.getName(), id), HttpStatus.OK);
        }
    }
}
