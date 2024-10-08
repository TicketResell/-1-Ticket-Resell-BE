package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.dto.CategoryDTO;
import com.teamseven.ticketresell.entity.CategoryEntity;
import com.teamseven.ticketresell.repository.CategoryRepository;
import com.teamseven.ticketresell.converter.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryConverter.toEntity(categoryDTO);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        return categoryConverter.toDTO(savedCategory);
    }

    public CategoryDTO getCategoryById(Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        return categoryEntity.map(categoryConverter::toDTO).orElse(null);
    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryConverter::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);
        if (categoryEntityOptional.isPresent()) {
            CategoryEntity existingCategory = categoryEntityOptional.get();
            existingCategory.setCategoryName(categoryDTO.getName());

            CategoryEntity updatedCategory = categoryRepository.save(existingCategory);
            return categoryConverter.toDTO(updatedCategory);
        }
        return null;
    }

    public boolean deleteCategory(Long id) {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);

        if (categoryEntityOptional.isPresent()) {
            categoryRepository.delete(categoryEntityOptional.get());
            return true;  // Trả về true nếu xóa thành công
        } else {
            return false;  // Trả về false nếu không tìm thấy category với id đã cho
        }
    }
}
