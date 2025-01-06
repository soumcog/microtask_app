package com.cts.controller;

import com.cts.entity.User;
import com.cts.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("admin1");
        User user2 = new User();
        user2.setUsername("employer1");
        List<User> users = Arrays.asList(user1, user2);
        when(adminService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin1");
        when(adminService.getUserById(1L)).thenReturn(user);

        ResponseEntity<User> response = adminController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(adminService, times(1)).getUserById(1L);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(adminService).deleteUser(1L);

        ResponseEntity<Void> response = adminController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(adminService, times(1)).deleteUser(1L);
    }

    @Test
    void testUpdateUserRole() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin1");
        user.setRole(User.Role.ADMIN);
        when(adminService.updateUserRole(1L, "EMPLOYER")).thenReturn(user);

        ResponseEntity<User> response = adminController.updateUserRole(1L, "EMPLOYER");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(adminService, times(1)).updateUserRole(1L, "EMPLOYER");
    }
}