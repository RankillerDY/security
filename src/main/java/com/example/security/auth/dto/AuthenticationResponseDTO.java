package com.example.security.auth.dto;

import lombok.Builder;


@Builder
public record AuthenticationResponseDTO(String token) {}
