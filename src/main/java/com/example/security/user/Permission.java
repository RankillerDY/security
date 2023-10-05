package com.example.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:create"),
    ADMIN_CREATE("admin:delete"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:create"),
    MANAGER_CREATE("management:delete"),
    MANAGER_DELETE("management:delete")
    ;

    @Getter
    private final String permission;
}
