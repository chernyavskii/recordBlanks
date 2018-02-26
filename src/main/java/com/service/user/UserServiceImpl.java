package com.service.user;

import com.dao.AgentDAO;
import com.dao.DocumentDAO;
import com.dao.RoleDAO;
import com.dao.UserDAO;
import com.model.Agent;
import com.model.Document;
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

    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private AgentDAO agentDAO;

    @Autowired
    private DocumentDAO documentDAO;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Transactional
    public User save(User user) {
        Role role = new Role();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        role.setName(ROLE_USER);
        role.setUser(user);
        roleDAO.save(role);
       return user;
    }

    @Override
    public User findById(Long id) {
        return userDAO.findOne(id);
    }

    @Override
    public Object deleteById(Long id) {
        User user = userDAO.findOne(id);
        agentDAO.delete(user.getAgents());
        roleDAO.delete(user.getRoles());
        documentDAO.delete(user.getDocuments());
        userDAO.delete(id);
        return "{\"success\":true}";
    }

    @Override
    public User updateById(User user, Long id) {
        User findUser = userDAO.findOne(id);
        findUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        findUser.setUsername(user.getUsername());
        findUser.setFirstName(user.getFirstName());
        findUser.setMiddleName(user.getMiddleName());
        findUser.setLastName(user.getLastName());
        findUser.setAddress(user.getAddress());
        findUser.setOrganization(user.getOrganization());
        findUser.setPosition(user.getPosition());
        findUser.setUnp(user.getUnp());
        findUser.setRs(user.getRs());
        findUser.setKs(user.getKs());
        findUser.setBank(user.getBank());
        findUser.setBik(user.getBik());
        findUser.setPhone(user.getPhone());
        return userDAO.save(findUser);
    }

    @Override
    public User findByUsername(String username) {
        getClass();
        return userDAO.findByUsername(username);
    }
}
