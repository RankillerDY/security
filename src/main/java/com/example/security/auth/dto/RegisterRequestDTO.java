package com.example.security.auth.dto;

import com.example.security.user.Role;
import lombok.Builder;

@Builder
public record RegisterRequestDTO(
         String email,
         String firstName,
         String lastName,
         String password,
         Role role
){}
