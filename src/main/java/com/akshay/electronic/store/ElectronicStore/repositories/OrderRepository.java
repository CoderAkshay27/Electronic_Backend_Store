package com.akshay.electronic.store.ElectronicStore.repositories;

import com.akshay.electronic.store.ElectronicStore.entities.Order;
import com.akshay.electronic.store.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);

}