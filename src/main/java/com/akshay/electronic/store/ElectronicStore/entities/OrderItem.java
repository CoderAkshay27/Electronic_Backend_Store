package com.akshay.electronic.store.ElectronicStore.entities;

import com.akshay.electronic.store.ElectronicStore.entities.Order;
import com.akshay.electronic.store.ElectronicStore.entities.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private Integer quantity;

    private Double totalPrice;
}