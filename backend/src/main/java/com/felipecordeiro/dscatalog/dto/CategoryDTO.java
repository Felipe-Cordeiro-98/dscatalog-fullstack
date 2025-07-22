package com.felipecordeiro.dscatalog.dto;

import com.felipecordeiro.dscatalog.entities.Category;

public record CategoryDTO(Long id, String name) {
    public CategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}
