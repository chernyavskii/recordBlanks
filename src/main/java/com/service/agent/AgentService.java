package com.service.agent;

import com.model.Agent;

import java.util.List;
import java.util.Set;

public interface AgentService {
    Set<Agent> getAllAgents(String username);
    Agent getAgentById(String username, Long id);
    Agent addAgent(String username, Agent agent);
    Agent deleteAgent(String username, Long id);
    Agent updateAgent(String username, Long id, Agent agent);
}

