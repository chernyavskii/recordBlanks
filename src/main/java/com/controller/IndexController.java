package com.controller;

import com.model.User;
import com.service.security.SecurityService;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    /*@RequestMapping(value = "/registration", method = RequestMethod.POST)
    public*//* @ResponseBody*//* String registration(@RequestBody User user) {
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());
        getClass();
*//*
        return "redirect:/ttt";

*//*
        return "redirect:/res";
    }*/

  /*  @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody Object addAgentInJSON(@RequestBody Agent agent) {
        return agentService.addAgent(agent);
    }*/

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public /*@ResponseBody*/ String testRegistration(@RequestBody User user){
        getClass();
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());
        return  "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
         getClass();
        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public /*@ResponseBody*/ String mainTest() {
        getClass();
/*
        return "main";
*/
        return "welcome";
    }

}
