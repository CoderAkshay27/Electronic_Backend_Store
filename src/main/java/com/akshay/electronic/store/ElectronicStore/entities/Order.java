package com.akshay.electronic.store.ElectronicStore.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    @ManyToOne
    private User user;

    private LocalDateTime orderDate;

    private Double orderAmount;

    private String orderStatus;

    private String paymentStatus;

    private String paymentMethod;

    private String billingName;

    private String billingPhone;

    private String billingAddress;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItems;
}