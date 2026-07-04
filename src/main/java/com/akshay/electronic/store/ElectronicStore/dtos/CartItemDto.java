package com.akshay.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Integer cartItemId;

    private Integer quantity;

    private Double totalPrice;

    private ProductDto product;
}