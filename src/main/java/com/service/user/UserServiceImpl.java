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

    public User registrationTest(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userDAO.save(user);
        return user;
    }

    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<Role>();
        roles.add(roleDAO.findOne(1L));
        user.setRoles(roles);

        userDAO.save(user);

       /* Set<User> users = new HashSet<User>();
        users.add(userDAO.findOne(user.getId()));

        Role ADMIN = roleDAO.findOne(1L);
        ADMIN.setUsers(users);*/

        Set<Role> roleHashSet = new HashSet<Role>();
        roleHashSet = user.getRoles();
        for(Role role : roleHashSet){
            role.getUsers().add(user);
            roleDAO.save(role);
        }
       /* roomSet = user.getRooms();
        for(Room room : roomSet){
            room.setCurrent_count(room.getCurrent_count()+1);
            roomDao.save(room);
        }
        userDao.save(user);*/

/*
        roleDAO.save(ADMIN);*/
/**
 return user;
 */
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

}
