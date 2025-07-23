package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.Role;

public record RoleDTO(Long id, String authority) {
    public RoleDTO(Role role) {
        this(role.getId(), role.getAuthority());
    }
}
