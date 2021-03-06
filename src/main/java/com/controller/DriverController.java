package com.controller;

import com.errors.Error;
import com.model.Driver;
import com.model.Role;
import com.service.driver.DriverService;
import com.service.user.UserService;
import com.validator.DriverValidator;
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
@RequestMapping(value = "api/v1/drivers")
@Api(tags = "Driver", description = "APIs for working with drivers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    DriverValidator driverValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of drivers", produces = MediaType.APPLICATION_JSON_VALUE, response = Driver.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of drivers", response = Driver.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "List of drivers are empty", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getAllDrivers(Principal principal)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Set<Driver> driverList = driverService.getAllDrivers(principal.getName());
        if (driverList.size() == 0) {
            error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(driverList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Driver by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Driver.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Driver", response = Driver.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Driver not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getDriverById(Principal principal, @PathVariable("id") Long id)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Driver driver = driverService.getDriverById(principal.getName(), id);
        if (driver == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Driver>(driver, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Add a new Driver", produces = MediaType.APPLICATION_JSON_VALUE, response = Driver.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return a new Driver", response = Driver.class),
            @ApiResponse(code = 201, message = "Return a new Driver", response = Driver.class),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not found", response = Error.class),
            @ApiResponse(code = 500, message = "Server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> addDriver(Principal principal, @RequestBody Driver driver, BindingResult bindingResult)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        driverValidator.validate(driver, bindingResult);
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.FIO_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(driverService.addDriver(principal.getName(), driver), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Driver by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Driver.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Driver",response = Driver.class),
            @ApiResponse(code = 201, message = "Return a new Driver", response = Driver.class),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Driver not found", response = Error.class),
            @ApiResponse(code = 500, message = "Server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> updateDriver(Principal principal, @PathVariable("id") Long id, @RequestBody Driver driver, BindingResult bindingResult)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!(role.getName().equals("ROLE_ADMIN") || (role.getName().equals("ROLE_USER") && driverService.getDriverById(principal.getName(), id) != null))) {
                error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
            else if(driverService.getDriver(id) == null) {
                error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        }
        driverValidator.validate(driver, bindingResult);
        if(bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.FIO_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<Driver>(driverService.updateDriver(principal.getName(), id, driver), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Driver by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Driver.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully",response = Driver.class),
            @ApiResponse(code = 204, message = "No content",response = Driver.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Driver not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> deleteDriver(Principal principal, @PathVariable("id") Long id)
    {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!(role.getName().equals("ROLE_ADMIN") || (role.getName().equals("ROLE_USER") && driverService.getDriverById(principal.getName(), id) != null))) {
                error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
            else if(driverService.getDriver(id) == null) {
                error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<Object>(driverService.deleteDriver(principal.getName(), id), HttpStatus.OK);
    }
}
