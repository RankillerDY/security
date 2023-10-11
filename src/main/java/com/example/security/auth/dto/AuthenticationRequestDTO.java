package com.example.security.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequestDTO(String email,String password) {
}
