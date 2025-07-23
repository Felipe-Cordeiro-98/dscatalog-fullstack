package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserResponseDTO(Long id,
                              String firstName,
                              String lastName,
                              String email,
                              Set<RoleDTO> roles
) {
    public UserResponseDTO(User user) {
        this(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet())
        );
    }
}