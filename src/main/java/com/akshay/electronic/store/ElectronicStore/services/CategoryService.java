package com.akshay.electronic.store.ElectronicStore.services;

import com.akshay.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;

public interface CategoryService {

    // Create Category
    CategoryDto createCategory(CategoryDto categoryDto);

    // Update Category
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    // Delete Category
    void deleteCategory(String categoryId);

    // Get Single Category
    CategoryDto getCategory(String categoryId);

    // Get All Categories
    PageableResponse<CategoryDto> getAllCategories(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );
}