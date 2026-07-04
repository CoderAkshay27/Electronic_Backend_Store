package com.akshay.electronic.store.ElectronicStore.controllers;

import com.akshay.electronic.store.ElectronicStore.dtos.ImageApiRespone;
import com.akshay.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.akshay.electronic.store.ElectronicStore.dtos.ProductDto;
import com.akshay.electronic.store.ElectronicStore.services.FileService;
import com.akshay.electronic.store.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    // Create Product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductDto productDto) {

        ProductDto createdProduct = productService.create(productDto);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Update Product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid @RequestBody ProductDto productDto,
            @PathVariable String productId) {

        ProductDto updatedProduct =
                productService.update(productDto, productId);

        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable String productId) {
        productService.delete(productId);

        return ResponseEntity.ok("Product deleted successfully");
    }

    // Get Single Product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(
            @PathVariable String productId) {

        ProductDto product = productService.get(productId);

        return ResponseEntity.ok(product);
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(

            @RequestParam(value = "pageNumber",
                    defaultValue = "0",
                    required = false)
            int pageNumber,

            @RequestParam(value = "pageSize",
                    defaultValue = "10",
                    required = false)
            int pageSize,

            @RequestParam(value = "sortBy",
                    defaultValue = "title",
                    required = false)
            String sortBy,

            @RequestParam(value = "sortDir",
                    defaultValue = "asc",
                    required = false)
            String sortDir) {

        PageableResponse<ProductDto> response =
                productService.getAll(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    // Get All Live Products
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(

            @RequestParam(value = "pageNumber",
                    defaultValue = "0")
            int pageNumber,

            @RequestParam(value = "pageSize",
                    defaultValue = "10")
            int pageSize,

            @RequestParam(value = "sortBy",
                    defaultValue = "title")
            String sortBy,

            @RequestParam(value = "sortDir",
                    defaultValue = "asc")
            String sortDir) {

        PageableResponse<ProductDto> response =
                productService.getAllLive(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    // Search Product By Title
    @GetMapping("/search/{title}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(

            @PathVariable String title,

            @RequestParam(value = "pageNumber",
                    defaultValue = "0")
            int pageNumber,

            @RequestParam(value = "pageSize",
                    defaultValue = "10")
            int pageSize,

            @RequestParam(value = "sortBy",
                    defaultValue = "title")
            String sortBy,

            @RequestParam(value = "sortDir",
                    defaultValue = "asc")
            String sortDir) {

        PageableResponse<ProductDto> response =
                productService.searchByTitle(
                        title,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    // Upload Product Image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageApiRespone> uploadImage(
            @RequestParam("productImage") MultipartFile file,
            @PathVariable("productId") String productId)
            throws IOException {

        String imageName =
                fileService.uploadFile(file, imagePath);

        ProductDto productDto =
                productService.get(productId);

        productDto.setProductImage(imageName);

        productService.update(productDto, productId);

        ImageApiRespone response =
                ImageApiRespone.builder()
                        .filename(imageName)
                        .success(true)
                        .message("Image uploaded successfully")
                        .status(HttpStatus.CREATED)
                        .build();

        return new ResponseEntity<>(response,
                HttpStatus.CREATED);
    }

    // Serve Product Image
    @GetMapping("/image/{productId}")
    public void serveImage(
            @PathVariable String productId,
            HttpServletResponse response)
            throws IOException {

        ProductDto productDto =
                productService.get(productId);

        InputStream resource =
                fileService.getResource(
                        imagePath,
                        productDto.getProductImage());

        String imageName =
                productDto.getProductImage();

        if (imageName.endsWith(".png")) {
            response.setContentType("image/png");
        } else {
            response.setContentType("image/jpeg");
        }

        StreamUtils.copy(
                resource,
                response.getOutputStream());
    }
}