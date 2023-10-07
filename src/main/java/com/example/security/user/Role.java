package com.example.security.user;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public enum Role {
  USER(Collections.emptySet()),
  ADMIN(
      Set.of(
          Permission.ADMIN_READ,
          Permission.ADMIN_UPDATE,
          Permission.ADMIN_CREATE,
          Permission.ADMIN_DELETE,
          Permission.MANAGER_READ,
          Permission.MANAGER_CREATE,
          Permission.MANAGER_DELETE,
          Permission.MANAGER_UPDATE)),
  MANAGER(
      Set.of(
          Permission.MANAGER_READ,
          Permission.MANAGER_CREATE,
          Permission.MANAGER_DELETE,
          Permission.MANAGER_UPDATE));

  @Getter private final Set<Permission> permission;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities =
        getPermission().stream()
            .map(permission1 -> new SimpleGrantedAuthority(permission1.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

    return authorities;
  }
}
