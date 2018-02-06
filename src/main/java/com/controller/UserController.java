package com.controller;

import com.model.User;
import com.service.agent.AgentService;
import com.service.user.UserService;
import com.utils.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @ApiOperation(value = "Get the list of users", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of users are empty", response = Error.class)})
    public @ResponseBody List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class),

    })
    public @ResponseBody Object findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class),

    })
    public @ResponseBody Object deleteById(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "{\"success\":true}";
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
