package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserRequestDTO(Long id,
                             String firstName,
                             String lastName,
                             String email,
                             String password,
                             Set<RoleDTO> roles
) {
    public UserRequestDTO(User user) {
        this(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet())
        );
    }
}