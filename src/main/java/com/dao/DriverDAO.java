package com.dao;

import com.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverDAO extends JpaRepository<Driver, Long> {

}
