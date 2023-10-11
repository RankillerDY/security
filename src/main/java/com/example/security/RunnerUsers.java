package com.example.security;

import com.example.security.auth.dto.RegisterRequestDTO;
import com.example.security.repository.UserRepository;
import com.example.security.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.example.security.user.Role.ADMIN;
import static com.example.security.user.Role.MANAGER;

@Configuration
public class RunnerUsers {

    @Autowired
    UserRepository repo;

    @Bean
    public CommandLineRunner loadInitialData(AuthenticationService auth) {

        return args -> {
            if (!repo.existsByEmail("baoit2002@gmail.com")) {
                var admin =
                        RegisterRequestDTO.builder()
                                .email("baoit2002@gmail.com")
                                .firstName("Bao")
                                .lastName("Dinh Gia")
                                .role(ADMIN)
                                .password("846130225")
                                .build();
                System.out.println("Admin authentication token " + auth.register(admin).token());
            }

            if (!repo.existsByEmail("khai2002@gmail.com")) {
                var manager =
                        RegisterRequestDTO.builder()
                                .email("khai2002@gmail.com")
                                .firstName("Khai")
                                .lastName("Nguyen Huy")
                                .role(MANAGER)
                                .password("1234")
                                .build();
                System.out.println("Manager authentication token " + auth.register(manager).token());
            }
        };
    }
}
