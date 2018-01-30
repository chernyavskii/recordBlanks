package com.dao;

import com.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentDAO extends JpaRepository<Agent, Long> {
}
