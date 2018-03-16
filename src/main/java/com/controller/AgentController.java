package com.controller;

import com.model.Agent;
import com.service.agent.AgentService;
import com.errors.Error;
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
@Api(value = "AgentsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    AgentValidator agentValidator;

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of agents", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return list of agents", response = Agent.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "List of agents are empty", response = Error.class)})
    public @ResponseBody
    ResponseEntity<?> getAgentsInJSON(Principal principal) {
        Error error;
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
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getAgentByIdInJSON(Principal principal, @PathVariable("id") Long id) {
        Error error;
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
            @ApiResponse(code = 409, message = "'unp' already exist in system", response = Error.class),
            @ApiResponse(code = 400, message = "'field' a field is empty", response = Error.class),
            @ApiResponse(code = 400, message = "'unp' a field must be 9 characters", response = Error.class),
            @ApiResponse(code = 500, message = "server error", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> addAgentInJSON(Principal principal, @RequestBody Agent agent, BindingResult bindingResult) {
        Error error;
        agentValidator.setMethod("post");
        agentValidator.validate(agent, bindingResult);
        if (bindingResult.hasErrors()) {
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
        } else {
            return new ResponseEntity<>(agentService.addAgent(principal.getName(), agent), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return Agent",response = Agent.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> updateAgentInJSON(Principal principal, @PathVariable("id") Long id, @RequestBody Agent agent, BindingResult bindingResult) {
        Error error;
        if (agentService.getAgentById(principal.getName(), id) == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            agentValidator.setMethod("update");
            agentValidator.validate(agent, bindingResult);
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
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class),

    })
    public @ResponseBody ResponseEntity<?> deleteAgentInJSON(Principal principal, @PathVariable("id") Long id) {
        Error error;
        if (agentService.getAgentById(principal.getName(), id) == null) {
            error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Object>(agentService.deleteAgent(principal.getName(), id), HttpStatus.OK);
        }
    }

}
