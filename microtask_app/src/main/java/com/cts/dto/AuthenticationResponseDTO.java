package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String token; // JWT token
    private String role;  // User role (ADMIN, EMPLOYER, WORKER)
    private Long userId;  // User ID
}