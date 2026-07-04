package com.akshay.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequest {
    @NotBlank(message = "Product ID is required")
    private String productId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}