package com.cts.controller;

import com.cts.dto.AuthenticationResponseDTO;
import com.cts.dto.LoginRequestDTO;
import com.cts.dto.RegisterRequestDTO;
import com.cts.exception.DuplicateResourceException;
import com.cts.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) { // Existing registration endpoint
           try {
               String response = authenticationService.register(request);
               return new ResponseEntity<>(response, HttpStatus.CREATED);
           } catch (DuplicateResourceException e) {
               return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
           }
             catch (Exception e) {
                 return new ResponseEntity<>("An error occurred during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
           }

    }



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(authenticationService.login(request));
    }

}