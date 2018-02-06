package com.service.user;

import com.dao.RoleDAO;
import com.dao.UserDAO;
import com.model.Role;
import com.model.User;
import com.utils.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public List<User> findAll() {

        return userDAO.findAll();
    }

    @Transactional
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        Role role = new Role();
        role.setName(ROLE_ADMIN);
        role.setUser(user);
        roleDAO.save(role);


       /* Set<Role> roles = new HashSet<Role>();
        roles.add(roleDAO.findOne(2L));
        user.setRoles(roles);

        userDAO.save(user);

        Set<Role> roleHashSet = new HashSet<Role>();
        roleHashSet = user.getRoles();
        for(Role role : roleHashSet){
            role.getUsers().add(user);
            roleDAO.save(role);
        }
        return user;*/
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

    @Override
    public void updateById(User user, Long id) {
        User findUser = userDAO.findOne(id);
        findUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        findUser.setUsername(user.getUsername());

        userDAO.save(findUser);
    }
}
