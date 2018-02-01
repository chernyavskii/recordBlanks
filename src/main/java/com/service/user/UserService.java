package com.service.user;

import com.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> findAll();
    User registrationTest(String username, String password);
    /*void*/User save(User user); /////////
    User findByUsername(String username);
}

