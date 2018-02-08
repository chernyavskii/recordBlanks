package com.service.agent;

import com.dao.AgentDAO;
import com.dao.UserDAO;
import com.model.Agent;
import com.errors.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public Agent getAgentById(String username, Long id)
    {
        Agent agent = new Agent();
        for(Agent agn : userDAO.findByUsername(username).getAgents())
        {
            if(id == agn.getId())
            {
                agent = agn;
            }
        }
        return  agent;

    }

    public Agent addAgent(String username, Agent agent)
    {
        Agent agnt = new Agent();
        agnt.setFirstName(agent.getFirstName());
        agnt.setMiddleName(agent.getMiddleName());
        agnt.setLastName(agent.getLastName());
        agnt.setUnp(agent.getUnp());
        agnt.setOrganization(agent.getOrganization());
        agnt.setPosition(agent.getPosition());
        agnt.setAddress(agent.getAddress());
        agnt.setUser(userDAO.findByUsername(username));
        return agentDAO.save(agnt);
    }

    public Agent deleteAgent(String username, Long id)
    {
        Agent agent = new Agent();
        for(Agent agn : userDAO.findByUsername(username).getAgents())
        {
            if(id == agn.getId())
            {
                agent = agn;
                agentDAO.delete(id);
            }
        }
        return  agent;
    }

    public Agent updateAgent(String username, Long id, Agent agent)
    {
        Agent agnt = agentDAO.findOne(id);
        agnt.setFirstName(agent.getFirstName());
        agnt.setMiddleName(agent.getMiddleName());
        agnt.setLastName(agent.getLastName());
        agnt.setUnp(agent.getUnp());
        agnt.setOrganization(agent.getOrganization());
        agnt.setPosition(agent.getPosition());
        agnt.setAddress(agent.getAddress());
        agnt.setUser(userDAO.findByUsername(username));
        return agentDAO.save(agnt);
    }
}
