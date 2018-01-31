package com.service.agent;

import com.model.Agent;

import java.util.List;

public interface AgentService {
    List<Agent> findAll();
    Agent findById(Long id);
    Agent addAgent(Agent agent);
    Agent deleteAgent(Long id);
    Agent updateAgent(Long id, Agent agent);
}

