
package com.cts.service;

import com.cts.dto.RegisterRequestDTO;

public interface AuthenticationService {
    String register(RegisterRequestDTO request); // Returns a message
}