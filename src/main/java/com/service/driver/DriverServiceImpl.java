package com.service.driver;

import com.dao.DriverDAO;
import com.dao.UserDAO;
import com.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverDAO driverDAO;

    @Autowired
    private UserDAO userDAO;

    public Driver addDriver(String username, Driver driver)
    {
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
}
