package com.tests.services;

import com.model.Agent;
import com.service.agent.AgentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AgentServiceTest {

    private AgentServiceImpl agentService;
    private Agent agent1;
    private Agent agent2;
    private Set<Agent> agents;

    @Before
    public void setUp() {
        agent1 = new Agent();
        agent2 = new Agent();
        agents = new HashSet<>();

        agent1.setId(1L);
        agent2.setId(2L);
        agent1.setLastName("Иванов");
        agent2.setLastName("Петров");

        agents.add(agent1);
        agents.add(agent2);

        agentService = mock(AgentServiceImpl.class);
    }

    @Test
    public void testGetAllAgents() {
        when(agentService.getAllAgents(anyString())).thenReturn(agents);

        Set<Agent> agentSet = agentService.getAllAgents("alexandr");

        assertNotNull(agentSet);
        assertTrue(agentSet.equals(agents));
    }

    @Test
    public void testGetAgentById() {
        when(agentService.getAgentById(anyString(), anyLong())).thenReturn(agent1);

        Agent agent = agentService.getAgentById("alexandr", 1L);

        assertNotNull(agent);
        assertTrue(agent.getId() == 1);
        assertEquals(agent.getLastName(), "Иванов");
    }

    @Test
    public void testGetAgent() {
        when(agentService.getAgent(anyLong())).thenReturn(agent1);

        Agent agent = agentService.getAgent(1L);

        assertNotNull(agent);
        assertTrue(agent.getId() == 1);
        assertEquals(agent.getLastName(), "Иванов");
    }

    @Test
    public void testAddAgent() {
        when(agentService.addAgent(anyString(), (Agent) any())).thenReturn(agent1);

        Agent agent = agentService.addAgent("alexandr", agent1);

        assertNotNull(agent);
        assertTrue(agent.getId() == 1);
        assertEquals(agent.getLastName(), "Иванов");
    }

    @Test
    public void testDeleteAgent() {
        when(agentService.deleteAgent(anyString(), anyLong())).thenReturn(agent1);

        Agent agent = agentService.deleteAgent("alexandr", 1L);

        assertNotNull(agent);
        assertTrue(agent.getId() == 1);
        assertEquals(agent.getLastName(), "Иванов");
    }

    @Test
    public void testUpdateAgent() {
        when(agentService.updateAgent(anyString(), anyLong(), (Agent) any())).thenReturn(agent2);

        Agent agent = agentService.updateAgent("alexandr", 1L, agent2);

        assertNotNull(agent);
        assertTrue(agent.getId() == 2);
        assertEquals(agent.getLastName(), "Петров");
    }

    @Test
    public void testCheckUnp() {
        when(agentService.checkUnp(anyString(), (Agent) any(), anyString())).thenReturn(true);

        Boolean value = agentService.checkUnp("alexandr", agent1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckBik() {
        when(agentService.checkBik(anyString(), (Agent) any(), anyString())).thenReturn(true);

        Boolean value = agentService.checkBik("alexandr", agent1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckKs() {
        when(agentService.checkKs(anyString(), (Agent) any(), anyString())).thenReturn(true);

        Boolean value = agentService.checkKs("alexandr", agent1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckRs() {
        when(agentService.checkRs(anyString(), (Agent) any(), anyString())).thenReturn(true);

        Boolean value = agentService.checkRs("alexandr", agent1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }
}
