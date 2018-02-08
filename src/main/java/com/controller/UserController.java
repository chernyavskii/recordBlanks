package com.controller;

import com.model.User;
import com.service.user.UserService;
import com.errors.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "users")
@Api(value = "UserControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of users", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of users", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of users are empty", response = Error.class)})
    public @ResponseBody
    ResponseEntity<?> findAll() {
        List<User> userList = userService.findAll();
        if (userList.size() == 0) {
            Error error = new Error(Error.LIST_USERS_EMPTY_MESSAGE, Error.LIST_USERS_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return User", response = User.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class),

    })
    public @ResponseBody ResponseEntity<?> findById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            Error error = new Error(Error.USER_NOT_FOUND_MESSAGE, Error.USER_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully",response = Object.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        if (userService.findById(id) == null) {
            Error error = new Error(Error.USER_NOT_FOUND_MESSAGE, Error.USER_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Object>(userService.deleteById(id), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Put the User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class),

    })
    public @ResponseBody Object updateById(@PathVariable("id") Long id, @RequestBody User user){
        userService.updateById(user, id);
        return "{\"success\":true}";
    }



}
