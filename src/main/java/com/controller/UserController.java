package com.controller;

import com.model.RequestWrapper;
import com.model.Role;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping(value = "users")
@Api(value = "UserControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserValidator userValidator;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    ////////////////////////// CRoSS ORIGIN ИЗМЕНИЛ
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Get list of users", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of users", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of users are empty", response = Error.class)})
    public @ResponseBody ResponseEntity<?> getAllUsers(Principal principal) {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!role.getName().equals("ROLE_ADMIN")) {
                Error error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
        }
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
    public @ResponseBody ResponseEntity<?> getUserById(Principal principal, @PathVariable("id") Long id) {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!role.getName().equals("ROLE_ADMIN")) {
                Error error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
        }
        User user = userService.findById(id);
        if (user == null) {
            Error error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody User addUser(Principal principal, @RequestBody RequestWrapper requestWrapper)
    {
        return userService.addUser(requestWrapper.getUser(), requestWrapper.getRole());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete User by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully", response = Object.class),
            @ApiResponse(code = 404, message = "User not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> deleteUserById(Principal principal, @PathVariable("id") Long id) {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!role.getName().equals("ROLE_ADMIN")) {
                Error error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
        }
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
    public @ResponseBody ResponseEntity<?> updateUserById(Principal principal, @PathVariable("id") Long id, @RequestBody RequestWrapper requestWrapper, BindingResult bindingResult) {
        requestWrapper.setUser_id(id);
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!(role.getName().equals("ROLE_ADMIN") || (role.getName().equals("ROLE_USER") && id == userService.findByUsername(principal.getName()).getId()))) {
                Error error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
        }
        userValidator.setMethod("update");
        userValidator.validate(requestWrapper, bindingResult);
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
                    default:
                        error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                        return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<User>(userService.updateById(requestWrapper.getUser(), id, requestWrapper.getRole()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<?> updatePassword(Principal principal, @RequestBody Map<String, String> passwords) {
        if (principal == null) {
            Error error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        if(!bCryptPasswordEncoder.matches(passwords.get("oldPassword"), userService.findByUsername(principal.getName()).getPassword())) {
            Error error = new Error(Error.WRONG_OLDPASSWORD_MESSAGE, Error.WRONG_OLDPASSWORD_STATUS, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
        }
        if(passwords.get("newPassword").length() < 8){
            Error error = new Error(" 'newPassword'" + ": " + Error.PASSWORD_LENGTH_MESSAGE, Error.PASSWORD_LENGTH_STATUS, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<User>(userService.updatePassword(principal.getName(), passwords.get("newPassword")), HttpStatus.OK);
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