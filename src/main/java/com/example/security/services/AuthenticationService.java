package com.example.security.services;

import com.example.security.auth.dto.AuthenticationResponseDTO;
import com.example.security.auth.dto.AuthenticationRequestDTO;
import com.example.security.auth.dto.RegisterRequestDTO;
import com.example.security.repository.UserRepository;
import com.example.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @Autowired private final UserRepository repo;
  @Autowired private final JwtService jwtService;
  @Autowired private final AuthenticationManager authenticationManager;

  private final PasswordEncoder passwordEncoder;

  public AuthenticationResponseDTO register(RegisterRequestDTO request) {
    if (!repo.existsByEmail(request.email())) {
      var user =
              User.builder()
                      .firstname(request.firstName())
                      .lastname(request.lastName())
                      .email(request.email())
                      .password(passwordEncoder.encode(request.password()))
                      .role(request.role())
                      .build();
      repo.save(user);
      var jwtToken = jwtService.generateToken(user);
      return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }
    return new AuthenticationResponseDTO(null);  //  <- Because a user with the same credentials is already registered, typically in applications, duplicate entries should be avoided.
  }

  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    var user = repo.findByEmail(request.email()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponseDTO.builder().token(jwtToken).build();
  }
}
