package com.akshay.electronic.store.ElectronicStore.services.impl;

import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.ProductDto;
import com.akshay.electronic.store.ElectronicStore.entities.Category;
import com.akshay.electronic.store.ElectronicStore.entities.Product;
import com.akshay.electronic.store.ElectronicStore.exception.ResourceNotFoundException;
import com.akshay.electronic.store.ElectronicStore.helper.Helper;
import com.akshay.electronic.store.ElectronicStore.repositories.CategoryRepository;
import com.akshay.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.akshay.electronic.store.ElectronicStore.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String productImage;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public ProductDto create(ProductDto productDto) {

        Product product = mapper.map(productDto, Product.class);

        product.setProductId(UUID.randomUUID().toString());
        Product saved = productRepository.save(product);

        return mapper.map(saved, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());

        Product saved = productRepository.save(product);

        return mapper.map(saved, ProductDto.class);
    }

    @Override
    public void delete(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        String path=product.getProductImage();

            if(path!=null && !path.isBlank() )
            {
                try{
                   Path imagePath= Paths.get(productImage,path);
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete product image", e);
                }
            }
        productRepository.delete(product);
    }

    @Override
    public ProductDto get(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository.findAll(pageable);

        return Helper.getPageableResponse(
                page,
                product -> mapper.map(product, ProductDto.class)
        );
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository.findByLiveTrue(pageable);

        return Helper.getPageableResponse(
                page,
                product -> mapper.map(product, ProductDto.class)
        );
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(
            String subTitle,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page =
                productRepository.findByTitleContainingIgnoreCase(
                        subTitle,
                        pageable
                );

        return Helper.getPageableResponse(
                page,
                product -> mapper.map(product, ProductDto.class)
        );
    }

    @Override
    public ProductDto productWithCategory(ProductDto productDto, String categoryId)
    {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("No Category with Id exists !!!"));
        Product product = mapper.map(productDto, Product.class);
        product.setProductId(UUID.randomUUID().toString());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return mapper.map(saved, ProductDto.class);
    }

    @Override
    public ProductDto updateProductCategory(String productId, String categoryId)
    {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with this Id not found !!!"));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category with this Id not found !!!"));

        product.setCategory(category);

        Product saved = productRepository.save(product);
        return mapper.map(saved, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductWithCategory(
            String category,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir){

        categoryRepository.findById(category)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No category with this Id!!!"));

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page =
                productRepository.findByCategory_CategoryId(category, pageable);

        return Helper.getPageableResponse(
                page,
                product -> mapper.map(product, ProductDto.class)
        );
    }
}