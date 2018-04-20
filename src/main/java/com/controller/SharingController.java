package com.controller;

import com.errors.Error;
import com.model.Document;
import com.model.RequestWrapper;
import com.service.sharing.SharingService;
import com.validator.SharingValidator;
import io.swagger.annotations.Api;
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

import java.security.Principal;
import java.util.Set;

@Controller
@CrossOrigin
@RequestMapping(value = "sharing")
@Api(tags = "Sharing", description = "APIs for working with shared documents", produces = MediaType.APPLICATION_JSON_VALUE)
public class SharingController {

    @Autowired
    private SharingService sharingService;

    @Autowired
    private SharingValidator sharingValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of shared documents", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of shared documents", response = Document.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "List of shared documents are empty", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getAllSharedDocuments(Principal principal)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Set<Document> documents = sharingService.getAllSharedDocuments(principal.getName());
        if (documents.size() == 0) {
            error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(documents, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get shared document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return shared Document", response = Document.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Shared Document not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getSharedDocumentById(Principal principal, @PathVariable("id") Long id)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Document document = sharingService.getSharedDocumentById(principal.getName(), id);
        if (document == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(document, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Share Document", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return shared Document", response = Document.class),
            @ApiResponse(code = 201, message = "Return shared Document", response = Document.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not found", response = Error.class),
            @ApiResponse(code = 500, message = "Server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> shareDocument(Principal principal, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        sharingValidator.validate(requestWrapper, bindingResult);
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.USER_IS_NOT_REGISTERED_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
                case Error.ENTITY_NOT_FOUND_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(sharingService.shareDocument(principal.getName(), requestWrapper.getAgent_id(), requestWrapper.getDocument_id()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete shared Document by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully",response = Document.class),
            @ApiResponse(code = 204, message = "No content",response = Document.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Shared Document not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> deleteSharedDocument(Principal principal, @PathVariable("id") Long id)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        if (sharingService.getSharedDocumentById(principal.getName(), id) == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(sharingService.deleteSharedDocument(principal.getName(), id), HttpStatus.OK);
        }
    }
}
