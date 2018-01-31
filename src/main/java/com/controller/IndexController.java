package com.controller;

import com.model.User;
import com.service.security.SecurityService;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@RequestBody User user) {
        Object o = getClass();
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());
        return "MISHA";
    }

}
