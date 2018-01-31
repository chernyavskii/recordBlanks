package com.service.agent;

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

    public Agent addAgent(Agent agent)
    {
        Agent agnt = new Agent();
        agnt.setFirstName(agent.getFirstName());
        agnt.setMiddleName(agent.getMiddleName());
        agnt.setLastName(agent.getLastName());
        agnt.setOrganization(agent.getOrganization());
        agnt.setPosition(agent.getPosition());
        return agentDAO.save(agnt);
    }

    public Agent deleteAgent(Long id)
    {
        Agent agnt = new Agent();
        agnt = agentDAO.findOne(id);
        agentDAO.delete(id);
        return agnt;
    }

    public Agent updateAgent(Long id, Agent agent)
    {
        Agent agnt = new Agent();
        agnt = agentDAO.findOne(id);
        agnt.setFirstName(agent.getFirstName());
        agnt.setMiddleName(agent.getMiddleName());
        agnt.setLastName(agent.getLastName());
        agnt.setOrganization(agent.getOrganization());
        agnt.setPosition(agent.getPosition());
        agentDAO.save(agnt);
        return agnt;
    }
}
