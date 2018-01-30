package com.service;

import com.dao.AgentDAO;
import com.model.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentDAO agentDAO;

    public List<Agent> findAll() {
        return agentDAO.findAll();
    }

    public Agent findById(Long id) {
        return agentDAO.findOne(id);
    }

    public Agent addAgent(String firstName, String middleName, String lastName, String organization, String position)
    {
        Agent agent = new Agent();
        agent.setId(2l);
        agent.setFirstName(firstName);
        agent.setMiddleName(middleName);
        agent.setLastName(lastName);
        agent.setOrganization(organization);
        agent.setPosition(position);
        agentDAO.save(agent);
        return agent;
    }
}
