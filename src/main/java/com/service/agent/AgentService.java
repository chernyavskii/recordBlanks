package com.service.agent;

import com.model.Agent;

import java.util.List;

public interface AgentService {
    List<Agent> findAll();
    Object findById(Long id);
    Object addAgent(Agent agent);
    Object deleteAgent(Long id);
    Object updateAgent(Long id, Agent agent);
}

