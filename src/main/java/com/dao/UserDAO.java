package com.dao;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUnp(String unp);
    User findByEmail(String email);
}
