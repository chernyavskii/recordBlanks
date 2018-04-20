package com.controller;

import com.model.Agent;
import com.model.RequestWrapper;
import com.model.Role;
import com.service.agent.AgentService;
import com.errors.Error;
import com.service.user.UserService;
import com.validator.AgentValidator;
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
@RequestMapping(value = "agents")
@Api(tags = "Agent", description = "APIs for working with agents", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private UserService userService;

    @Autowired
    AgentValidator agentValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of agents", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of agents", response = Agent.class, responseContainer = "Set"),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "List of agents are empty", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getAllAgents(Principal principal) {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Set<Agent> agentList = agentService.getAllAgents(principal.getName());
        if (agentList.size() == 0) {
            error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(agentList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Agent", response = Agent.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getAgentById(Principal principal, @PathVariable("id") Long id) {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        Agent agent = agentService.getAgentById(principal.getName(), id);
        if (agent == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Agent>(agent, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Add a new Agent", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return a new Agent", response = Agent.class),
            @ApiResponse(code = 201, message = "Return a new Agent", response = Agent.class),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not found", response = Error.class),
            @ApiResponse(code = 500, message = "Server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> addAgent(Principal principal, @RequestBody Agent agent, BindingResult bindingResult) {
        Error error;
        RequestWrapper requestWrapper = new RequestWrapper();
        requestWrapper.setAgent(agent);
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        agentValidator.setMethod("post");
        agentValidator.validate(requestWrapper, bindingResult);
        if (bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
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
                case Error.DUPLICATED_ENTITY_MESSAGE:
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
            return new ResponseEntity<>(agentService.addAgent(principal.getName(), agent), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Agent",response = Agent.class),
            @ApiResponse(code = 201, message = "Return Agent", response = Agent.class),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class),
            @ApiResponse(code = 500, message = "Server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> updateAgent(Principal principal, @PathVariable("id") Long id, @RequestBody Agent agent, BindingResult bindingResult) {
        RequestWrapper requestWrapper = new RequestWrapper();
        agent.setId(id);
        requestWrapper.setAgent(agent);
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!(role.getName().equals("ROLE_ADMIN") || (role.getName().equals("ROLE_USER") && agentService.getAgentById(principal.getName(), id) != null))) {
                error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
            else if(agentService.getAgent(id) == null) {
                error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        }
        agentValidator.setMethod("update");
        agentValidator.validate(requestWrapper, bindingResult);
        if(bindingResult.hasErrors()) {
            switch (bindingResult.getFieldError().getDefaultMessage()) {
                case Error.FIO_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.UNP_BIK_LENGTH_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.RS_KS_LENGTH_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.PHONE_INCORRECT_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                case Error.DUPLICATED_ENTITY_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.CONFLICT.value());
                    return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
                case Error.EMPTY_FIELD_MESSAGE:
                    error = new Error(" '" + bindingResult.getFieldError().getField() + "'" + ": " + bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getCode(), HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
                default:
                    error = new Error(Error.SERVER_ERROR_MESSAGE, Error.SERVER_ERROR_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity<Agent>(agentService.updateAgent(principal.getName(), id, agent), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully",response = Agent.class),
            @ApiResponse(code = 204, message = "No content",response = Agent.class),
            @ApiResponse(code = 401, message = "User is not authorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> deleteAgent(Principal principal, @PathVariable("id") Long id) {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        for(Role role : userService.findByUsername(principal.getName()).getRoles()) {
            if(!(role.getName().equals("ROLE_ADMIN") || (role.getName().equals("ROLE_USER") && agentService.getAgentById(principal.getName(), id) != null))) {
                error = new Error(Error.NO_ACCESS_MESSAGE, Error.NO_ACCESS_STATUS, HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<Error>(error, HttpStatus.FORBIDDEN);
            }
            else if(agentService.getAgent(id) == null) {
                error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<Object>(agentService.deleteAgent(principal.getName(), id), HttpStatus.OK);
    }
}
