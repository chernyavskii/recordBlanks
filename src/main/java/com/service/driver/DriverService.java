package com.service.driver;

import com.model.Driver;
import java.util.Set;

public interface DriverService {
    Set<Driver> getAllDrivers(String username);
    Driver getDriverById(String username, Long id);
    Driver addDriver(String username, Driver driver);
    Driver deleteDriver(String username, Long id);
    Driver updateDriver(String username, Long id, Driver driver);
}

