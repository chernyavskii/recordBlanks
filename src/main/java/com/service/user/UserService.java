package com.service.user;

import com.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User save(User user);
    User findById(Long id);
    void deleteById(Long id);
    void updateById(User user, Long id);
}

