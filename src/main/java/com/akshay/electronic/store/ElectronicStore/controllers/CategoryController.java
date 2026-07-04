package com.akshay.electronic.store.ElectronicStore.controllers;

import com.akshay.electronic.store.ElectronicStore.dtos.CategoryDto;
import com.akshay.electronic.store.ElectronicStore.dtos.ImageApiRespone;
import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.ProductDto;
import com.akshay.electronic.store.ElectronicStore.services.CategoryService;
import com.akshay.electronic.store.ElectronicStore.services.FileService;
import com.akshay.electronic.store.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Value("${category.image.path}")
    private String imagePath;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;

    // Create Category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto createdCategory =
                categoryService.createCategory(categoryDto);

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Update Category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable String categoryId) {

        CategoryDto updatedCategory =
                categoryService.updateCategory(categoryDto, categoryId);

        return ResponseEntity.ok(updatedCategory);
    }

    // Delete Category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable String categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok("Category deleted successfully");
    }

    // Get Single Category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable String categoryId) {

        CategoryDto category =
                categoryService.getCategory(categoryId);

        return ResponseEntity.ok(category);
    }

    // Get All Categories
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(

            @RequestParam(value = "pageNumber", defaultValue = "0", required = false)
            int pageNumber,

            @RequestParam(value = "pageSize", defaultValue = "10", required = false)
            int pageSize,

            @RequestParam(value = "sortBy", defaultValue = "title", required = false)
            String sortBy,

            @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
            String sortDir) {

        PageableResponse<CategoryDto> response =
                categoryService.getAllCategories(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageApiRespone> uploadImage(
            @RequestParam("categoryImage") MultipartFile file,
            @PathVariable("categoryId") String categoryId)
            throws IOException {

        // Upload image to disk
        String imageName = fileService.uploadFile(file, imagePath);

        // Fetch category
        CategoryDto categoryDto = categoryService.getCategory(categoryId);

        // Update image name in database
        categoryDto.setCoverImage(imageName);
        categoryService.updateCategory(categoryDto, categoryId);

        // Prepare response
        ImageApiRespone response = ImageApiRespone.builder()
                .filename(imageName)
                .success(true)
                .message("Image uploaded successfully")
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void serveImage(
            @PathVariable String categoryId,
            HttpServletResponse response) throws IOException {

        CategoryDto categoryDto = categoryService.getCategory(categoryId);

        InputStream resource =
                fileService.getResource(imagePath, categoryDto.getCoverImage());

        String imageName = categoryDto.getCoverImage();

        if (imageName.endsWith(".png")) {
            response.setContentType("image/png");
        } else {
            response.setContentType("image/jpeg");
        }
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable("categoryId") String categoryId, @RequestBody ProductDto product) {
        ProductDto productDto = productService.productWithCategory(product, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProductCategory(
            @PathVariable String categoryId,
            @PathVariable String productId) {

        ProductDto productDto = productService.updateProductCategory(productId, categoryId);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageableResponse<ProductDto> response =
                productService.getAllProductWithCategory(
                        categoryId,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir
                );

        return ResponseEntity.ok(response);
    }
}