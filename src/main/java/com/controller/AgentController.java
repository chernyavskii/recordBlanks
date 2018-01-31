package com.controller;

import com.model.Agent;
import com.service.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody List<Agent> getAgentsInJSON() {
        return agentService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Agent getAgentByIdInJSON(@PathVariable("id") Long id) {
        return agentService.findById(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody Agent addAgentInJSON(@RequestParam String firstName, @RequestParam String middleName, @RequestParam String lastName, @RequestParam String organization, @RequestParam String position) {
        return agentService.addAgent(firstName, middleName, lastName, organization, position);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Agent deleteAgentInJSON(@PathVariable("id") Long id) {
        return agentService.deleteAgent(id);
    }
}
