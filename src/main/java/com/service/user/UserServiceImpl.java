package com.service.user;

import com.dao.RoleDAO;
import com.dao.UserDAO;
import com.model.Role;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Transactional
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<Role>();
        roles.add(roleDAO.findOne(2L));
        user.setRoles(roles);

        userDAO.save(user);

        Set<Role> roleHashSet = new HashSet<Role>();
        roleHashSet = user.getRoles();
        for(Role role : roleHashSet){
            role.getUsers().add(user);
            roleDAO.save(role);
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        return userDAO.findOne(id);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.delete(id);
    }

   /* @Override
    public User updateById(User user, Long id) {
        User findUser = userDAO.findOne(id);
        findUser.setPassword(user.getPassword());
        findUser.setUsername(user.getPassword());

        userDAO.save(findUser);
        //need fields to Agents

        return findUser;
    }*/



  /*  public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }*/



}
