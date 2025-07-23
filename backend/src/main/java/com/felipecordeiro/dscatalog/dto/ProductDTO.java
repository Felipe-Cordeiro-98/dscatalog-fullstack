package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.Category;
import com.felipecordeiro.dscatalog.entities.Product;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductDTO(Long id,
                         String name,
                         String description,
                         Double price,
                         String imgUrl,
                         Instant date,
                         Set<CategoryDTO> categories) {

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