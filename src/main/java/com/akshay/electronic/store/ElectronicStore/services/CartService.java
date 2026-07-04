package com.akshay.electronic.store.ElectronicStore.services;

import com.akshay.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.akshay.electronic.store.ElectronicStore.dtos.CartDto;
import com.akshay.electronic.store.ElectronicStore.dtos.UpdateCartItemRequest;

public interface CartService {

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    void removeItemFromCart(String userId,int cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);

    void updateCartItemQuantity(
            String userId,
            Integer cartItemId,
            UpdateCartItemRequest request);

}
