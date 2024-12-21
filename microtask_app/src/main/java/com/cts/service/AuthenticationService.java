package com.cts.service;

import com.cts.dto.AuthenticationResponseDTO;
import com.cts.dto.LoginRequestDTO;
import com.cts.dto.RegisterRequestDTO;

public interface AuthenticationService {
    String register(RegisterRequestDTO request);
    AuthenticationResponseDTO login(LoginRequestDTO request);
}	