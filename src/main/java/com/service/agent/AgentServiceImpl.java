package com.service.agent;

import com.dao.AgentDAO;
import com.utils.Error;
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

    public Object findById(Long id)
    {
        Agent agnt = new Agent();
        agnt = agentDAO.findOne(id);
        if(agnt == null)
        {
            return (new Error("Null value", "null_value", 400));
        }
        return agnt;
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

    public Object deleteAgent(Long id)
    {
        Agent agnt = new Agent();
        agnt = agentDAO.findOne(id);
        if(agnt == null)
        {
            return (new Error("Null value", "null_value", 400));
        }
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
