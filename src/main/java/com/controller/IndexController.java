package com.controller;

import com.model.User;
import com.service.security.SecurityService;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody Object testRegistration(@RequestBody User user){
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());

        return  "{\"success\":true}";
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
