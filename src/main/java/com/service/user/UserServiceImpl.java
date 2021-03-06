package com.service.user;

import com.dao.AgentDAO;
import com.dao.DocumentDAO;
import com.dao.RoleDAO;
import com.dao.UserDAO;
import com.model.Role;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

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

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User save(User user) {
        Role role = new Role();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
        userDAO.save(user);
        role.setName(ROLE_USER);
        role.setUser(user);
        roleDAO.save(role);
       return user;
    }

    public User addUser(User user, String r) {
        Role role = new Role();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
        userDAO.save(user);
        role.setName(r);
        role.setUser(user);
        roleDAO.save(role);
        return user;
    }

    public User findById(Long id) {
        return userDAO.findOne(id);
    }

    public Object deleteById(Long id) {
        User user = userDAO.findOne(id);
        agentDAO.delete(user.getAgents());
        roleDAO.delete(user.getRoles());
        documentDAO.delete(user.getDocuments());
        userDAO.delete(id);
        return user;
    }

    public User updateById(User user, Long id, String r) {
        User findUser = userDAO.findOne(id);
        findUser.setEmail(user.getEmail());
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
        userDAO.save(findUser);
        for(Role rol : findUser.getRoles()) {
            rol.setName(r);
            rol.setUser(findUser);
            roleDAO.save(rol);
        }
        return findUser;
    }

    public User updatePassword(String username, String newPassword) {
        User user = userDAO.findByUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return userDAO.save(user);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public Boolean checkEmail(User usr, String method)
    {
        Iterator<User> iterator = userDAO.findAll().iterator();
        for(User user : userDAO.findAll()){
            if(method.equals("post")) {
                if (user.getEmail().equals(usr.getEmail())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(user.getId() == usr.getId()) {
                    iterator.next();
                }
                else if(user.getEmail().equals(usr.getEmail())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkUsername(User usr, String method)
    {
        Iterator<User> iterator = userDAO.findAll().iterator();
        for(User user : userDAO.findAll()){
            if(method.equals("post")) {
                if (user.getUsername().equals(usr.getUsername())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(user.getId() == usr.getId()) {
                    iterator.next();
                }
                else if(user.getUsername().equals(usr.getUsername())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkUnp(User usr, String method)
    {
        Iterator<User> iterator = userDAO.findAll().iterator();
        for(User user : userDAO.findAll()){
            if(method.equals("post")) {
                if (user.getUnp().equals(usr.getUnp())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(user.getId() == usr.getId()) {
                    iterator.next();
                }
                else if(user.getUnp().equals(usr.getUnp())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkRs(User usr, String method)
    {
        Iterator<User> iterator = userDAO.findAll().iterator();
        for(User user : userDAO.findAll()){
            if(method.equals("post")) {
                if (user.getRs().equals(usr.getRs())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(user.getId() == usr.getId()) {
                    iterator.next();
                }
                else if(user.getRs().equals(usr.getRs())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkKs(User usr, String method)
    {
        Iterator<User> iterator = userDAO.findAll().iterator();
        for(User user : userDAO.findAll()){
            if(method.equals("post")) {
                if (user.getKs().equals(usr.getKs())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(user.getId() == usr.getId()) {
                    iterator.next();
                }
                else if(user.getKs().equals(usr.getKs())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkBik(User usr, String method)
    {
        Iterator<User> iterator = userDAO.findAll().iterator();
        for(User user : userDAO.findAll()){
            if(method.equals("post")) {
                if (user.getBik().equals(usr.getBik())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(user.getId() == usr.getId()) {
                    iterator.next();
                }
                else if(user.getBik().equals(usr.getBik())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }
}
