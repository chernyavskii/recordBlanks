package com.dao;

import com.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface AgentDAO extends JpaRepository<Agent, Long> {
    Agent findByUnp(String unp);
    Set<Agent> findAllByUnp(String unp);
}
