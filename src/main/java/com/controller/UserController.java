package com.controller;

import com.model.User;
import com.service.user.UserService;
import com.errors.Error;
import com.validator.UserValidator;
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
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "users")
@Api(value = "UserControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserValidator userValidator;

    ////////////////////////// CRoSS ORIGIN ИЗМЕНИЛ
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Get list of users", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of users", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of users are empty", response = Error.class)})
    public @ResponseBody
    ResponseEntity<?> findAll() {
        List<User> userList = userService.findAll();
        if (userList.size() == 0) {
            Error error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return User", response = User.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class)
    })
    public @ResponseBody
    ResponseEntity<?> findById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully", response = Object.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class)
    })
    public @ResponseBody
    ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        if (userService.findById(id) == null) {
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Object>(userService.deleteById(id), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return updated User", response = User.class),
            @ApiResponse(code = 409, message = "'username' already exist in system", response = Error.class),
            @ApiResponse(code = 400, message = "'field' a field is empty", response = Error.class),
            @ApiResponse(code = 400, message = "'password' a field must be bellow 8 characters", response = Error.class),
            @ApiResponse(code = 400, message = "'username' a field must be bellow 5 characters", response = Error.class),
            @ApiResponse(code = 500, message = "server error", response = Error.class)
    })
    public @ResponseBody
    ResponseEntity<?> updateById(@PathVariable("id") Long id, @RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (userService.findById(id) == null) {
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            if (bindingResult.hasErrors()) {
                Error error;
                switch (bindingResult.getFieldError().getDefaultMessage()) {
                    case Error.EMPTY_FIELD_MESSAGE:
                        error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                        return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                    case Error.DUPLICATED_ENTITY_MESSAGE:
                        error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                        return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
                    case Error.PASSWORD_LENGTH_MESSAGE:
                        error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                        return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                    case Error.USERNAME_LENGTH_MESSAGE:
                        error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                        return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                    default:
                        error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                        return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<User>(userService.updateById(user, id), HttpStatus.OK);
        }
    }
  /*  @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> currentUserName(@PathVariable String username) {
        return new ResponseEntity<User>(userService.findByUsername(username), HttpStatus.OK);
    }*/

   /* @RequestMapping(value = " /order/{orderId}", method=RequestMethod.GET)
    public String getOrder(@PathVariable String orderId){
//fetch order
    }*/
}