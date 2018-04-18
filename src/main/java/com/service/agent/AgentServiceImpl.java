package com.service.agent;

import com.dao.AgentDAO;
import com.dao.UserDAO;
import com.model.Agent;
import com.errors.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentDAO agentDAO;

    @Autowired
    private UserDAO userDAO;

    public Set<Agent> getAllAgents(String username) {
        return userDAO.findByUsername(username).getAgents();
    }

    public Agent getAgentById(String username, Long id) {
        for(Agent agn : userDAO.findByUsername(username).getAgents())
        {
            if(id == agn.getId())
            {
                return agentDAO.findOne(id);
            }
        }
        return  null;
    }

    public Agent getAgent(Long id) {
        return agentDAO.findOne(id);
    }

    public Agent addAgent(String username, Agent agent) {
        Agent agnt = new Agent();
        agnt.setFirstName(agent.getFirstName());
        agnt.setMiddleName(agent.getMiddleName());
        agnt.setLastName(agent.getLastName());
        agnt.setUnp(agent.getUnp());
        agnt.setOrganization(agent.getOrganization());
        agnt.setPosition(agent.getPosition());
        agnt.setAddress(agent.getAddress());
        agnt.setRs(agent.getRs());
        agnt.setKs(agent.getKs());
        agnt.setBank(agent.getBank());
        agnt.setBik(agent.getBik());
        agnt.setPhone(agent.getPhone());
        agnt.setUser(userDAO.findByUsername(username));
        return agentDAO.save(agnt);
    }

    public Agent deleteAgent(String username, Long id) {
        Agent agent = new Agent();
        for(Agent agn : userDAO.findByUsername(username).getAgents())
        {
            if(id == agn.getId())
            {
                agent = agn;
                agentDAO.delete(id);
                return  agent;
            }
        }
        return  null;
    }

    public Agent updateAgent(String username, Long id, Agent agent) {
        Agent agnt = agentDAO.findOne(id);
        agnt.setFirstName(agent.getFirstName());
        agnt.setMiddleName(agent.getMiddleName());
        agnt.setLastName(agent.getLastName());
        agnt.setUnp(agent.getUnp());
        agnt.setOrganization(agent.getOrganization());
        agnt.setPosition(agent.getPosition());
        agnt.setAddress(agent.getAddress());
        agnt.setRs(agent.getRs());
        agnt.setKs(agent.getKs());
        agnt.setBank(agent.getBank());
        agnt.setBik(agent.getBik());
        agnt.setPhone(agent.getPhone());
        return agentDAO.save(agnt);
    }

    public Boolean checkUnp(String username, Agent agnt, String method) {
        Iterator<Agent> iterator = userDAO.findByUsername(username).getAgents().iterator();
        for(Agent agent : userDAO.findByUsername(username).getAgents()){
            if(method.equals("post")) {
                if (agent.getUnp().equals(agnt.getUnp())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(agent.getId() == agnt.getId()) {
                    iterator.next();
                }
                else if(agent.getUnp().equals(agnt.getUnp())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkRs(String username, Agent agnt, String method) {
        Iterator<Agent> iterator = userDAO.findByUsername(username).getAgents().iterator();
        for(Agent agent : userDAO.findByUsername(username).getAgents()){
            if(method.equals("post")) {
                if (agent.getRs().equals(agnt.getRs())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(agent.getId() == agnt.getId()) {
                    iterator.next();
                }
                else if(agent.getRs().equals(agnt.getRs())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkKs(String username, Agent agnt, String method) {
        Iterator<Agent> iterator = userDAO.findByUsername(username).getAgents().iterator();
        for(Agent agent : userDAO.findByUsername(username).getAgents()){
            if(method.equals("post")) {
                if (agent.getKs().equals(agnt.getKs())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(agent.getId() == agnt.getId()) {
                    iterator.next();
                }
                else if(agent.getKs().equals(agnt.getKs())){
                    return true;
                }
                else{
                    iterator.next();
                }
            }
        }
        return false;
    }

    public Boolean checkBik(String username, Agent agnt, String method) {
        Iterator<Agent> iterator = userDAO.findByUsername(username).getAgents().iterator();
        for(Agent agent : userDAO.findByUsername(username).getAgents()){
            if(method.equals("post")) {
                if (agent.getBik().equals(agnt.getBik())) {
                    return true;
                } else {
                    iterator.next();
                }
            }
            if(method.equals("update")) {
                if(agent.getId() == agnt.getId()) {
                    iterator.next();
                }
                else if(agent.getBik().equals(agnt.getBik())){
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
