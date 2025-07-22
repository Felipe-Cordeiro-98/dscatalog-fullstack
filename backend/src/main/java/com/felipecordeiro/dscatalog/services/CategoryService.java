package com.felipecordeiro.dscatalog.services;

import com.felipecordeiro.dscatalog.dto.CategoryDTO;
import com.felipecordeiro.dscatalog.entities.Category;
import com.felipecordeiro.dscatalog.repositories.CategoryRepository;
import com.felipecordeiro.dscatalog.services.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }
}
