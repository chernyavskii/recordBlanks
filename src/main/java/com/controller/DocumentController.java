package com.controller;

import com.model.Document;
import com.model.RequestWrapper;
import com.model.Role;
import com.service.document.DocumentService;
import com.errors.Error;
import com.service.user.UserService;
import com.validator.DocumentValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

@Controller
@CrossOrigin
@RequestMapping(value = "documents")
@Api(value = "DocumentControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentController
{
    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @Autowired DocumentValidator documentValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of documents", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Document.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "List of documents are empty", response = Error.class)})
    public @ResponseBody ResponseEntity<?> getAllDocuments(Principal principal)
    {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Set<Document> documentList = documentService.getAllDocuments(principal.getName());
        if (documentList.size() == 0) {
            Error error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Set<Document>>(documentList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    //@ApiOperation(value = "Get the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Document", response = Document.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class)
    })
    public ResponseEntity<?> getDocumentById(Principal principal, @PathVariable("id") Long id, @RequestParam(value = "type", required = false) String type) throws IOException, java.lang.Exception {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Document document = documentService.getDocumentById(principal.getName(), id);
        if(document == null){
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        }
        else {
            HttpHeaders responseHeaders = new HttpHeaders();
            if("pdf".equals(type)) {
                responseHeaders.setContentLength(document.getDocumentPdf().length);
                responseHeaders.set("Content-disposition", "attachment; filename=" + document.getName());
                responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
                return new ResponseEntity<byte[]>(document.getDocumentPdf(), responseHeaders, HttpStatus.OK);
            }
            if("png".equals(type)) {
                responseHeaders.setContentLength(document.getDocumentPng().length);
                responseHeaders.set("Content-disposition", "attachment; filename=" + document.getName());
                responseHeaders.setContentType(MediaType.valueOf("image/png"));
                return new ResponseEntity<byte[]>(document.getDocumentPng(), responseHeaders, HttpStatus.OK);
            }
            responseHeaders.setContentLength(document.getDocumentExcel().length);
            responseHeaders.set("Content-disposition", "attachment; filename=" + document.getName());
            responseHeaders.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            return new ResponseEntity<byte[]>(document.getDocumentExcel(), responseHeaders, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/tn", method = RequestMethod.POST)
    @ApiOperation(value = "Write to file", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Document",response = Document.class),
            @ApiResponse(code = 400, message = "'field' a field is empty", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> addDocumentTN(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) throws IOException {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        documentValidator.setType("tn");
        documentValidator.validate(requestWrapper, bindingResult);
        Error error;
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.ENTITY_NOT_FOUND_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.FIELD_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(documentService.addDocumentTN(principal.getName(), requestWrapper.getDocumentName(), requestWrapper.getAgent_id(), requestWrapper.getProducts()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully",response = Document.class),
            @ApiResponse(code = 404, message = "Document not found", response = Error.class),
    })
    public @ResponseBody ResponseEntity<?> deleteDocument(Principal principal, @PathVariable("id") Long id) throws java.io.IOException {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!(role.getName().equals("ROLE_ADMIN") || (role.getName().equals("ROLE_USER") && documentService.getDocumentById(principal.getName(), id) != null))) {
                Error error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
            else if(documentService.getDocument(id) == null) {
                Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<Object>(documentService.deleteDocument(principal.getName(), id), HttpStatus.OK);
    }

    @RequestMapping(value = "/ttn", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addDocumentTTN(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) throws IOException
    {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        documentValidator.setType("ttn");
        documentValidator.validate(requestWrapper, bindingResult);
        Error error;
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.ENTITY_NOT_FOUND_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.FIELD_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(documentService.addDocumentTTN(principal.getName(), requestWrapper.getDocumentName(), requestWrapper.getAgent_id(), requestWrapper.getDriver_id(), requestWrapper.getProducts()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/aspr", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addDocumentASPR(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) throws IOException
    {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        documentValidator.setType("aspr");
        documentValidator.validate(requestWrapper, bindingResult);
        Error error;
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.ENTITY_NOT_FOUND_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.FIELD_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(documentService.addDocumentASPR(principal.getName(), requestWrapper.getDocumentName(), requestWrapper.getAgent_id(), requestWrapper.getWorks()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/sf", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addDocumentSF(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) throws IOException
    {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        documentValidator.setType("sf");
        documentValidator.validate(requestWrapper, bindingResult);
        Error error;
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.ENTITY_NOT_FOUND_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.FIELD_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<>(documentService.addDocumentSF(principal.getName(), requestWrapper.getDocumentName(), requestWrapper.getAgent_id(), requestWrapper.getProducts()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/convert/{id}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addPdf(Principal principal, @PathVariable("id") Long id, @RequestBody Map<String, String> documents) throws IOException
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Document document = documentService.getDocumentById(principal.getName(), id);
        if(document == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        }
        if("".equals(documents.get("documentPdf"))) {
            error = new Error(" 'documentPdf': " + Error.EMPTY_FIELD_MESSAGE, Error.EMPTY_FIELD_STATUS, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
        }
        if("".equals(documents.get("documentPng"))) {
            error = new Error(" 'documentPng': " + Error.EMPTY_FIELD_MESSAGE, Error.EMPTY_FIELD_STATUS, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
        }
        byte[] pdf = Base64.decodeBase64(documents.get("documentPdf").getBytes());
        byte[] png = Base64.decodeBase64(documents.get("documentPng").getBytes());
        return new ResponseEntity<>(documentService.convert(principal.getName(), id, pdf, png), HttpStatus.OK);
    }
}
