package com.akshay.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private Integer orderItemId;
    private ProductDto product;
    private Integer quantity;
    private Double totalPrice;
}