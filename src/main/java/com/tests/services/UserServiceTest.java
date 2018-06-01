package com.tests.services;

import com.model.User;
import com.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private UserServiceImpl userService;

    private User user1;

    private User user2;

    private List<User> users;

    @Before
    public void setUp() {
        user1 = new User();
        user2 = new User();
        users = new ArrayList<>();

        user1.setId(1L);
        user2.setId(2L);

        user1.setUsername("alexandr");
        user2.setUsername("ivan");

        users.add(user1);
        users.add(user2);

        userService = mock(UserServiceImpl.class);
    }

    @Test
    public void testFindAll() {
        when(userService.findAll()).thenReturn(users);

        List<User> userList = userService.findAll();

        assertNotNull(userList);
        assertTrue(userList.equals(userList));
    }

    @Test
    public void testFindById() {
        when(userService.findById(anyLong())).thenReturn(user1);

        User user = userService.findById(1L);

        assertNotNull(user);
        assertTrue(user.getId() == 1);
        assertEquals(user.getUsername(), "alexandr");
    }

    @Test
    public void testFindByUsername() {
        when(userService.findByUsername(anyString())).thenReturn(user1);

        User user = userService.findByUsername("alexandr");

        assertNotNull(user);
        assertTrue(user.getId() == 1);
        assertEquals(user.getUsername(), "alexandr");
    }

    @Test
    public void testSave() {
        when(userService.save((User) any())).thenReturn(user1);

        User user = userService.save(user1);

        assertNotNull(user);
        assertTrue(user.getId() == 1);
        assertEquals(user.getUsername(), "alexandr");
    }

    @Test
    public void testAddUser() {
        when(userService.addUser((User) any(), anyString())).thenReturn(user1);

        User user = userService.addUser(user1, "ROLE_USER");

        assertNotNull(user);
        assertTrue(user.getId() == 1);
        assertEquals(user.getUsername(), "alexandr");
    }

    @Test
    public void testDeleteById() {
        when(userService.deleteById(anyLong())).thenReturn(user1);

        Object o = userService.deleteById(1L);

        assertNotNull(0);
    }

    @Test
    public void testUpdateById() {
        when(userService.updateById((User) any(), anyLong(), anyString())).thenReturn(user2);

        User user = userService.updateById(user1,1L, "ROLE_USER");

        assertNotNull(user);
        assertTrue(user.getId() == 2);
        assertEquals(user.getUsername(), "ivan");
    }

    @Test
    public void testUpdatePassword() {
        when(userService.updatePassword(anyString(), anyString())).thenReturn(user1);

        User user = userService.updatePassword("alexandr", "12345678");

        assertNotNull(user);
        assertTrue(user.getId() == 1);
        assertEquals(user.getUsername(), "alexandr");
    }

    @Test
    public void testCheckUnp() {
        when(userService.checkUnp((User) any(), anyString())).thenReturn(true);

        Boolean value = userService.checkUnp(user1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckBik() {
        when(userService.checkBik((User) any(), anyString())).thenReturn(true);

        Boolean value = userService.checkBik(user1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckKs() {
        when(userService.checkKs((User) any(), anyString())).thenReturn(true);

        Boolean value = userService.checkKs(user1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckUsername() {
        when(userService.checkUsername((User) any(), anyString())).thenReturn(true);

        Boolean value = userService.checkUsername(user1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckEmail() {
        when(userService.checkEmail((User) any(), anyString())).thenReturn(true);

        Boolean value = userService.checkEmail(user1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }

    @Test
    public void testCheckRs() {
        when(userService.checkRs((User) any(), anyString())).thenReturn(true);

        Boolean value = userService.checkRs(user1, "post");

        assertNotNull(value);
        assertTrue(value == true);
    }
}
