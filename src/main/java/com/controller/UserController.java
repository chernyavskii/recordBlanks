package com.controller;

import com.model.Agent;
import com.model.Role;
import com.model.User;
import com.service.agent.AgentService;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgentService agentService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        binder.registerCustomEditor(Set.class, "agents", new CustomCollectionEditor(Set.class) {
            protected Object convertElement(Object element) {
                if (element instanceof String) {
                    Set<Agent> agents = new HashSet<>();
                    agents.add(agentService.findById(Long.parseLong(element.toString())));

                    return agents;
                }
                return null;
            }
        });
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Object findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteById(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "{\"success\":true}";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody Object updateById(@PathVariable("id") Long id, @RequestBody User user){
        return "{\"success\":true}";
    }



}
