package com.cts.service;

import com.cts.entity.User;
import com.cts.exception.ResourceNotFoundException;
import com.cts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUserRole(Long id, String newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Validate the new role
        if (!isValidRole(newRole)) {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }

        user.setRole(User.Role.valueOf(newRole));
        return userRepository.save(user);
    }

    private boolean isValidRole(String role) {
        for (User.Role validRole : User.Role.values()) {
            if (validRole.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}