package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.User;
import com.felipecordeiro.dscatalog.services.validation.UserUpdateValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

@UserUpdateValid
public record UserUpdateDTO(
        @NotBlank(message = "O primeiro nome é obrigatório")
        @Size(min = 2, max = 50, message = "O primeiro nome deve ter entre 2 e 50 caracteres")
        String firstName,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres")
        String lastName,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotEmpty(message = "É necessário informar pelo menos um papel (role)")
        Set<RoleDTO> roles
) {
    public UserUpdateDTO(User user) {
        this(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet())
        );
    }
}