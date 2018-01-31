package com.service.agent;

import com.model.Agent;

import java.util.List;

public interface AgentService {
    List<Agent> findAll();
    Agent findById(Long id);
    Agent addAgent(String firstName, String middleName, String lastName, String organization, String position);
    Agent deleteAgent(Long id);
}

