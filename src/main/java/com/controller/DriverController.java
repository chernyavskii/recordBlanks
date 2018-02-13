package com.controller;

import com.model.Driver;
import com.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping(value = "drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody Driver addDriver(Principal principal, @RequestBody Driver driver)
    {
        return driverService.addDriver(principal.getName(), driver);
    }
}
