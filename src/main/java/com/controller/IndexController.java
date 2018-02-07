package com.controller;

import com.model.User;
import com.service.security.SecurityService;
import com.service.user.UserService;
import com.errors.Error;
import com.validator.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/")
@Api(value = "IndexControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ApiOperation(value = "New User registration", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = User.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)})
    public @ResponseBody ResponseEntity<?> registration(@RequestBody User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            Error error = new Error(" '"+bindingResult.getFieldError().getField()+"'"+": "+bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
        }
        else {
            userService.save(user);
            securityService.autoLogin(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
           /* List<ObjectError> objectErrors = bindingResult.getAllErrors();
            String objectError1 = bindingResult.getFieldError().getField();
            Object asd = bindingResult.getFieldError().getRejectedValue();*/
           /* for (ObjectError objectError : objectErrors) {
                Error a = new Error();
                a.setCode(objectError.getCode());
                a.setMessage(objectError.getDefaultMessage());

                a1.add(a);
            }
            return new ResponseEntity<List<Error>>(a1,HttpStatus.FORBIDDEN);*/

        //}
        /*else {
            getClass();
            if (user.getPassword().equals("") && user.getUsername().equals("")
                    && user.getAddress().equals("") && user.getOrganization().equals("")
                    && user.getUnp().equals("") && user.getFirstName().equals("")
                    && user.getLastName().equals("") && user.getPosition().equals("")
                    && user.getMiddleName().equals("")) {

                return new ResponseEntity<>(new Error(Error.NOT_FOUND_MESSAGE, Error.NOT_FOUND_CODE), HttpStatus.FORBIDDEN);
            } else {
                userService.save(user);
                securityService.autoLogin(user.getUsername(), user.getPassword());
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return null;*/
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
