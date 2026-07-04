package com.akshay.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartItemRequest {
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}