package com.service.user;

import com.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> findAll();
    User save(User user);
    User findById(Long id);
    void deleteById(Long id);
/*
    User updateById(User user, Long id);
*/
/*
    User findByUsername(String username);
*/
}

