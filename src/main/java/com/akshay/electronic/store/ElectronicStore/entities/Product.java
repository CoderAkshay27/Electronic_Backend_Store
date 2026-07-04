package com.akshay.electronic.store.ElectronicStore.entities;

import com.akshay.electronic.store.ElectronicStore.dtos.CategoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(length = 1000)
    private String description;
    private double price;
    private Date addedDate;
    private double discountedPrice;
    private int quantity;
    private boolean live;
    private boolean stock;
    private String productImage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}