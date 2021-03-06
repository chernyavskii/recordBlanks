package com.service.driver;

import com.dao.DriverDAO;
import com.dao.UserDAO;
import com.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverDAO driverDAO;

    @Autowired
    private UserDAO userDAO;

    public Set<Driver> getAllDrivers(String username) {
        return userDAO.findByUsername(username).getDrivers();
    }

    public Driver getDriverById(String username, Long id) {
        for(Driver drv : userDAO.findByUsername(username).getDrivers())
        {
            if(id == drv.getId())
            {
                return driverDAO.findOne(id);
            }
        }
        return  null;
    }

    public Driver getDriver(Long id) {
        return driverDAO.findOne(id);
    }

    public Driver addDriver(String username, Driver driver) {
        Driver drv = new Driver();
        drv.setFirstName(driver.getFirstName());
        drv.setMiddleName(driver.getMiddleName());
        drv.setLastName(driver.getLastName());
        drv.setCarModel(driver.getCarModel());
        drv.setCarNumber(driver.getCarNumber());
        drv.setTrailerModel(driver.getTrailerModel());
        drv.setTrailerNumber(driver.getTrailerModel());
        drv.setUser(userDAO.findByUsername(username));
        return driverDAO.save(drv);
    }

    public Driver deleteDriver(String username, Long id) {
        Driver driver = driverDAO.findOne(id);
        driverDAO.delete(id);
        return  driver;
    }

    public Driver updateDriver(String username, Long id, Driver driver) {
        Driver drv = driverDAO.findOne(id);
        drv.setFirstName(driver.getFirstName());
        drv.setMiddleName(driver.getMiddleName());
        drv.setLastName(driver.getLastName());
        drv.setCarModel(driver.getCarModel());
        drv.setCarNumber(driver.getCarNumber());
        drv.setTrailerModel(driver.getTrailerModel());
        drv.setTrailerNumber(driver.getTrailerNumber());
        return driverDAO.save(drv);
    }
}
