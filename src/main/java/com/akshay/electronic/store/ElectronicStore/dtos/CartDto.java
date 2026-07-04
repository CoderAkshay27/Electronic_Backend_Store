package com.akshay.electronic.store.ElectronicStore.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private String cartId;

    private UserDTO user;

    private LocalDateTime createdDate;

    private List<CartItemDto> items = new ArrayList<>();
}