package com.felipe.dscatalog.services;

import com.felipe.dscatalog.dto.CategoryDTO;
import com.felipe.dscatalog.entities.Category;
import com.felipe.dscatalog.repositories.CategoryRepository;
import com.felipe.dscatalog.services.exceptions.ResouceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResouceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
       try {
           Category entity = repository.getReferenceById(id);
           entity.setName(dto.getName());
           entity = repository.save(entity);
           return new CategoryDTO(entity);
       } catch (EntityNotFoundException e) {
            throw new ResouceNotFoundException("Id not found " + id);
       }
    }
}

