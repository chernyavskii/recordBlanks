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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@CrossOrigin
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
            @ApiResponse(code = 200, message = "Return a new User",response = User.class),
            @ApiResponse(code = 409, message = "'username' already exist in system", response = Error.class),
            @ApiResponse(code = 400, message = "'field' a field is empty", response = Error.class),
            @ApiResponse(code = 400, message = "'password' a field must be bellow 8 characters", response = Error.class),
            @ApiResponse(code = 400, message = "'username' a field must be bellow 5 characters", response = Error.class),
            @ApiResponse(code = 500, message = "server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> registration(@RequestBody User user, BindingResult bindingResult)  {
        userValidator.setMethod("post");
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            Error error;
            switch(bindingResult.getFieldError().getDefaultMessage())
            {
                case Error.DUPLICATED_ENTITY_MESSAGE :
                    error = new Error(" '"+bindingResult.getFieldError().getField()+"'"+": "+bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
                case Error.EMPTY_FIELD_MESSAGE :
                    error = new Error(" '"+bindingResult.getFieldError().getField()+"'"+": "+bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.PASSWORD_LENGTH_MESSAGE :
                    error = new Error(" '"+bindingResult.getFieldError().getField()+"'"+": "+bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.PASSWORD_DO_NOT_MATCH_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.USERNAME_LENGTH_MESSAGE :
                    error = new Error(" '"+bindingResult.getFieldError().getField()+"'"+": "+bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.FIO_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.UNP_BIK_LENGTH_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.RS_KS_LENGTH_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.PHONE_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default :
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            userService.save(user);
            securityService.autoLogin(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> testLogin(Principal principal){
        User user = userService.findByUsername(principal.getName());
        securityService.autoLogin(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @CrossOrigin
    @ApiOperation(value = "User login ", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return a User",response = User.class),
            @ApiResponse(code = 403, message = "login or password is incorrect", response = Error.class)})
    public @ResponseBody ResponseEntity<?> login(@RequestBody User user,  BindingResult bindingResult) {
        securityService.autoLogin(user.getUsername(), user.getPassword());
        if(SecurityContextHolder.getContext().getAuthentication().getCredentials() != ""){
            //return new ResponseEntity<>(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), HttpStatus.OK);
            return new ResponseEntity<>(userService.findByUsername(user.getUsername()), HttpStatus.OK);
        }
        else {
            Error error = new Error(Error.LOGIN_INCORRECT_MESSAGE, Error.LOGIN_INCORRECT_STATUS, HttpStatus.FORBIDDEN.value());
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    @ApiOperation(value = "User logout ", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logout user", response = User.class),
            @ApiResponse(code = 404, message = "user do not logged-in", response = Error.class)})
    public @ResponseBody ResponseEntity<?> logout (HttpServletRequest request, HttpServletResponse response) {
        if(SecurityContextHolder.getContext().getAuthentication().getCredentials() != "") {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            Error error = new Error(Error.USER_DO_NOT_LOGGEDIN_MESSAGE, Error.USER_DO_NOT_LOGGEDIN_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public /*@ResponseBody*/ String mainTest() {

        return "welcome";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String fgf() {

        return "index";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String as() {

        return "main";
    }

}
