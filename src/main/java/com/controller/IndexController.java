package com.controller;

import com.model.User;
import com.service.security.SecurityService;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
public class IndexController {
/*
    http://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
*/
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody Object registration(@RequestBody User user){
        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());

        return "{\"success\":true}";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Object login(@RequestBody User user) {
        securityService.autoLogin(user.getUsername(), user.getPassword());

        return user;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public @ResponseBody Object logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "{\"success\":true}";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public /*@ResponseBody*/ String mainTest() {

        return "welcome";
    }

}
