package com.akshay.electronic.store.ElectronicStore.services;

import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.ProductDto;
import com.akshay.electronic.store.ElectronicStore.entities.Product;

public interface ProductService {

    ProductDto create(ProductDto productDto);

    ProductDto update(ProductDto productDto, String productId);

    void delete(String productId);

    ProductDto get(String productId);

    PageableResponse<ProductDto> getAll(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir);

    PageableResponse<ProductDto> getAllLive(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir);

    PageableResponse<ProductDto> searchByTitle(
            String subTitle,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir);

    ProductDto productWithCategory(ProductDto productDto, String categoryId);

    ProductDto updateProductCategory(String productId, String categoryId);

    PageableResponse<ProductDto> getAllProductWithCategory(String category, int pageNumber, int pageSize, String sortDir, String sortBy);
}