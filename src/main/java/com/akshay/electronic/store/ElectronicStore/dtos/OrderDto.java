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
public class OrderDto {

    private String orderId;

    private UserDTO user;

    private LocalDateTime orderDate;

    private Double orderAmount;

    private String orderStatus;

    private String paymentStatus;

    private String paymentMethod;

    private String billingName;

    private String billingPhone;

    private String billingAddress;

    private List<OrderItemDto> orderItems
            = new ArrayList<>();
}