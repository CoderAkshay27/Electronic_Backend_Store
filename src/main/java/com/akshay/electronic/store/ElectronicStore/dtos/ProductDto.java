package com.akshay.electronic.store.ElectronicStore.dtos;

import com.akshay.electronic.store.ElectronicStore.entities.Category;
import com.akshay.electronic.store.ElectronicStore.services.ProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto
{
    private String productId;
    @NotBlank
    private String title;
    private String description;

    private double price;

    private double discountedPrice;

    private int quantity;

    private boolean live;

    private boolean stock;

    private String productImage;
    private CategoryDto category;
}