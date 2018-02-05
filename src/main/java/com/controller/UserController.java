package com.controller;

import com.model.User;
import com.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody User findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteById(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "{\"success\":true}";
    }

  /*  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody User updateById(@PathVariable("id") Long id){
        return userService.updateById(id);
    }*/



}
