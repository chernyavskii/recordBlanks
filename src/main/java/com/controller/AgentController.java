package com.controller;

import com.model.Agent;
import com.service.agent.AgentService;
import com.errors.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Get the list of agents", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "List of agents are empty", response = Error.class)})
    public @ResponseBody Set<Agent> getAgentsInJSON(Principal principal) {
        return agentService.getAllAgents(principal.getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response", response = Agent.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class)
    })
    public @ResponseBody Object getAgentByIdInJSON(Principal principal, @PathVariable("id") Long id) {
        return agentService.getAgentById(principal.getName(), id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Add a new Agent", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 422, message = "Wrong parameters", response = Error.class)
    })
    public @ResponseBody Object addAgentInJSON(Principal principal, @RequestBody Agent agent) {
        return agentService.addAgent(principal.getName(), agent);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete the Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class),

    })
    public @ResponseBody Object deleteAgentInJSON(Principal principal, @PathVariable("id") Long id) {
        return agentService.deleteAgent(principal.getName(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Put the Agent by ID", produces = MediaType.APPLICATION_JSON_VALUE, response = Agent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response",response = Agent.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
            @ApiResponse(code = 404, message = "Agent not found", response = Error.class),

    })
    public @ResponseBody Object updateAgentInJSON(Principal principal, @PathVariable("id") Long id, @RequestBody Agent agent) {
        return agentService.updateAgent(principal.getName(), id, agent);
    }
}
