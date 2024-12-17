package com.cts.service;

import com.cts.dto.AuthenticationResponseDTO;
import com.cts.dto.LoginRequestDTO;
import com.cts.dto.RegisterRequestDTO;
import com.cts.entity.User;
import com.cts.exception.DuplicateResourceException;
import com.cts.repository.UserRepository;
import com.cts.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    public String register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists.");
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
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }



        @Override
    public AuthenticationResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}