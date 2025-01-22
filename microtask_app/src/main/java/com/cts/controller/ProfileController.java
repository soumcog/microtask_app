package com.cts.controller;

import com.cts.entity.User;
import com.cts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    // Get current user's profile
    @GetMapping
    public ResponseEntity<User> getProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    // Update current user's profile
    @PutMapping
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.updateUserProfile(username, updatedUser);
        return ResponseEntity.ok(user);
    }
}