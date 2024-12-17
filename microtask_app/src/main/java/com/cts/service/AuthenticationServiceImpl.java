package com.cts.service;


import com.cts.dto.RegisterRequestDTO;
import com.cts.entity.User;
import com.cts.exception.DuplicateResourceException;
import com.cts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists."); // Prevent duplicate usernames
        }

        try {
             User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(request.getRole())
                    .build();
            userRepository.save(user);
            return "User registered successfully!";
        } catch (Exception e) {
            // Handle other potential exceptions (e.g., database errors)
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);  // Or a custom exception
        }
    }
}