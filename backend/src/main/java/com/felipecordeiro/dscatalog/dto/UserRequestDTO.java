package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.User;
import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.stream.Collectors;

public record UserRequestDTO(
        @NotBlank(message = "O primeiro nome é obrigatório")
        @Size(min = 2, max = 50, message = "O primeiro nome deve ter entre 2 e 50 caracteres")
        String firstName,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres")
        String lastName,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        @NotEmpty(message = "É necessário informar pelo menos um papel (role)")
        Set<RoleDTO> roles
) {
    public UserRequestDTO(User user) {
        this(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet())
        );
    }
}
