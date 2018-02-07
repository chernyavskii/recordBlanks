package com.controller;

import com.model.User;
import com.service.security.SecurityService;
import com.service.user.UserService;
import com.utils.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/")
@Api(value = "IndexControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ApiOperation(value = "New User registration", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)})
    public @ResponseBody User registration(@RequestBody User user){
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());

        return user;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "User login ", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)})
    public @ResponseBody User login(@RequestBody User user) {
        securityService.autoLogin(user.getUsername(), user.getPassword());
        return user;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    @ApiOperation(value = "User logout ", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response", response = User.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)})
    public @ResponseBody Object logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "{\"success\":true}";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public /*@ResponseBody*/ String mainTest() {

        return "welcome";
    }

}
