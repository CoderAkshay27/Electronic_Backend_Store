package com.akshay.electronic.store.ElectronicStore.services.impl;

import com.akshay.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.entities.Category;
import com.akshay.electronic.store.ElectronicStore.exception.ResourceNotFoundException;
import com.akshay.electronic.store.ElectronicStore.helper.Helper;
import com.akshay.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.akshay.electronic.store.ElectronicStore.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    @Value("${category.image.path}")
    private String categoryImagePath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = dtoToEntity(categoryDto);

        Category savedCategory = categoryRepository.save(category);

        return entityToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : " + categoryId));

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);

        return entityToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : " + categoryId));

        String imageName = category.getCoverImage();

        if (imageName != null && !imageName.isBlank()) {
            try {
                Path imagePath = Paths.get(categoryImagePath, imageName);
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete category image", e);
            }
        }

        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto getCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : " + categoryId));

        return entityToDto(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Category> page = categoryRepository.findAll(pageable);

        return Helper.getPageableResponse(
                page,
                this::entityToDto
        );
    }

    private Category dtoToEntity(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }

    private CategoryDto entityToDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }
}