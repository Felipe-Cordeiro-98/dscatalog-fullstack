package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.Category;
import com.felipecordeiro.dscatalog.entities.Product;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductDTO(
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 60 caracteres")
        String name,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 5, message = "A descrição deve ter no mínimo 5 caracteres")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser um valor positivo")
        Double price,

        String imgUrl,

        @PastOrPresent(message = "A data não pode ser futura")
        Instant date,

        @NotEmpty(message = "O produto deve ter pelo menos uma categoria")
        Set<CategoryDTO> categories
) {
    public ProductDTO(Product product) {
        this(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                product.getDate(),
                Set.of());
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImgUrl(),
                product.getDate(),
                categories.stream().map(CategoryDTO::new).collect(Collectors.toSet()));
    }
}
