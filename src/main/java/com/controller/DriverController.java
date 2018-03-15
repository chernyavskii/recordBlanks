package com.controller;

import com.model.Driver;
import com.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Set;

@Controller
@CrossOrigin
@RequestMapping(value = "drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody Set<Driver> getAllDrivers(Principal principal)
    {
        return driverService.getAllDrivers(principal.getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Driver getDriverById(Principal principal, @PathVariable("id") Long id)
    {
        return driverService.getDriverById(principal.getName(), id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody Driver addDriver(Principal principal, @RequestBody Driver driver)
    {
        return driverService.addDriver(principal.getName(), driver);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Driver deleteDriver(Principal principal, @PathVariable("id") Long id)
    {
        return driverService.deleteDriver(principal.getName(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody Driver updateDriver(Principal principal, @PathVariable("id") Long id, @RequestBody Driver driver)
    {
        return driverService.updateDriver(principal.getName(), id, driver);
    }
}
