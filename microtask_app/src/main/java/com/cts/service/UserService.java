package com.cts.service;

import com.cts.entity.User;

public interface UserService {
    User getUserByUsername(String username); // Fetch user by username
    User updateUserProfile(String username, User updatedUser); // Update user profile
}