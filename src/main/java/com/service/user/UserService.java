package com.service.user;

import com.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User save(User user);
    User findById(Long id);
    Object deleteById(Long id);
    User updateById(User user, Long id);
    User findByUsername(String username);
    Boolean checkUnp(User usr, String method);
    Boolean checkRs(User usr, String method);
    Boolean checkKs(User usr, String method);
    Boolean checkBik(User usr, String method);
}

