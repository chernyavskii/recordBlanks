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
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "agents")
@Api(value = "AgentsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired AgentValidator agentValidator;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of agents", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class, responseContainer = "Set"),
            @ApiResponse(code = 404, message = "List of agents are empty", response = Error.class)})
    public @ResponseBody ResponseEntity<?> getAgentsInJSON(Principal principal) {
        Error error;
        if(principal == null){
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        else {
            Set<Agent> agentList = agentService.getAllAgents(principal.getName());
            if (agentList.size() == 0) {
                error = new Error(Error.LIST_ENTITIES_EMPTY_MESSAGE, Error.LIST_ENTITIES_EMPTY_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(agentList, HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response", response = Agent.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class)
    })
    public @ResponseBody ResponseEntity<?> getAgentByIdInJSON(Principal principal, @PathVariable("id") Long id) {
        Error error;
        if(principal == null){
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        }
        else {
           Agent agent = agentService.getAgentById(principal.getName(), id);
           if(agent == null){
               error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
               return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
           }
           else {
               return new ResponseEntity<Agent>(agent, HttpStatus.OK);
           }
        }
    }
////////////////////////////////// VALIDATION СДЕЛАЙ ДЛЯ АГЕНТА
    ///////////////////////////
    ///////////////////////////
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Add a new Agent", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)
    })
    public @ResponseBody Object addAgentInJSON(Principal principal, @RequestBody Agent agent, BindingResult bindingResult) {
        agentValidator.validate(agent, bindingResult);
        if(bindingResult.hasErrors()){
            getClass();
        }
        return agentService.addAgent(principal.getName(), agent);
    }

    ////////////////////////// PUT ПЕРЕТЕСТИРУЙ ЗАНОВО
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Put Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class),

    })
    public @ResponseBody Object updateAgentInJSON(Principal principal, @PathVariable("id") Long id, @RequestBody Agent agent) {
        return agentService.updateAgent(principal.getName(), id, agent);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class),

    })
    public @ResponseBody Object deleteAgentInJSON(Principal principal, @PathVariable("id") Long id) {
        Error error;
        if (principal == null) {
            error = new Error(Error.UNAUTHORIZED_MESSAGE, Error.UNAUTHORIZED_STATUS, HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
        } else {
            if (agentService.getAgentById(principal.getName(), id) == null) {
                error = new Error(Error.ENTITY_NOT_FOUND_MESSAGE, Error.ENTITY_NOT_FOUND_STATUS, HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Object>(agentService.deleteAgent(principal.getName(), id), HttpStatus.OK);
            }
        }
    }
}
