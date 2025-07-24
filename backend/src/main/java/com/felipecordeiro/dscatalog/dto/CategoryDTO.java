package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDTO(
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
        String name
) {
    public CategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}