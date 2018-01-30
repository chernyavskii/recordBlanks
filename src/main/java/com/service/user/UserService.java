package com.service.user;

import com.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User registrationTest(String username, String password);
}

