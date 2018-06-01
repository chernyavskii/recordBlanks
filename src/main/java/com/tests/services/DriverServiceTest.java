package com.tests.services;

import com.model.Driver;
import com.service.driver.DriverServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceTest {
    private DriverServiceImpl driverService;

    private Driver driver1;

    private Driver driver2;

    private Set<Driver> drivers;

    @Before
    public void setUp() {
        driver1 = new Driver();
        driver2 = new Driver();
        drivers = new HashSet<>();

        driver1.setId(1L);
        driver2.setId(2L);

        driver1.setLastName("Иванов");
        driver2.setLastName("Петров");

        drivers.add(driver1);
        drivers.add(driver2);

        driverService = mock(DriverServiceImpl.class);
    }

    @Test
    public void testGetAllDrivers() {
        when(driverService.getAllDrivers(anyString())).thenReturn(drivers);

        Set<Driver> driverList = driverService.getAllDrivers("alexandr");

        assertNotNull(driverList);
        assertTrue(driverList.equals(drivers));
    }

    @Test
    public void testGetDriverById() {
        when(driverService.getDriverById(anyString(), anyLong())).thenReturn(driver1);

        Driver driver = driverService.getDriverById("alexandr", 1L);

        assertNotNull(driver);
        assertTrue(driver.getId() == 1);
        assertEquals(driver.getLastName(), "Иванов");
    }

    @Test
    public void testAddDriver() {
        when(driverService.addDriver(anyString(), (Driver) any())).thenReturn(driver1);

        Driver driver = driverService.addDriver("alexandr", driver1);

        assertNotNull(driver);
        assertTrue(driver.getId() == 1);
        assertEquals(driver.getLastName(), "Иванов");
    }

    @Test
    public void testDeleteDriver() {
        when(driverService.deleteDriver(anyString(), anyLong())).thenReturn(driver1);

        Driver driver = driverService.deleteDriver("alexandr", 1L);

        assertNotNull(driver);
        assertTrue(driver.getId() == 1);
        assertEquals(driver.getLastName(), "Иванов");
    }

    @Test
    public void testUpdateDriver() {
        when(driverService.updateDriver(anyString(), anyLong(), (Driver) any())).thenReturn(driver2);

        Driver driver = driverService.updateDriver("alexandr", 1L, driver2);

        assertNotNull(driver);
        assertTrue(driver.getId() == 2);
        assertEquals(driver.getLastName(), "Петров");
    }
}
