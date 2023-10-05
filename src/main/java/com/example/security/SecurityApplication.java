package com.example.security;

import com.example.security.auth.RegisterRequest;
import com.example.security.services.AuthenticationService;
import com.example.security.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.security.user.Role.ADMIN;
import static com.example.security.user.Role.MANAGER;

@SpringBootApplication
public class SecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(AuthenticationService auth) {
    return args -> {
      var admin =
          RegisterRequest.builder()
              .email("baoit2002@gmail.com")
              .firstName("Bao")
              .lastName("Dinh Gia")
              .role(ADMIN)
              .password("846130225")
              .build();
      System.out.println("Authentication token " + auth.register(admin).getToken());

      var manager =
              RegisterRequest.builder()
                      .email("khai2002@gmail.com")
                      .firstName("Khai")
                      .lastName("Nguyen Huy")
                      .role(MANAGER)
                      .password("1234")
                      .build();
      System.out.println("Authentication token " + auth.register(manager).getToken());

    };
  }
}
