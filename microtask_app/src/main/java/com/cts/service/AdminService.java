package com.cts.service;

import com.cts.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    User updateUserRole(Long id, String newRole);
}