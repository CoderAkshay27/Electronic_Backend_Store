package com.akshay.electronic.store.ElectronicStore.controllers;

import com.akshay.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.akshay.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.akshay.electronic.store.ElectronicStore.dtos.CartDto;
import com.akshay.electronic.store.ElectronicStore.dtos.UpdateCartItemRequest;
import com.akshay.electronic.store.ElectronicStore.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    // Add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @Valid @RequestBody AddItemToCartRequest request) {

        CartDto cartDto = cartService.addItemToCart(userId, request);

        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<String> updateCartItemQuantity(@PathVariable String userId, @PathVariable Integer cartItemId, @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItemQuantity(userId, cartItemId, request);
        return ResponseEntity.ok("Cart item quantity updated successfully");
    }

    // Remove a specific item from cart
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int cartItemId) {
        cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Item Deleted Succesfully.").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // Clear complete cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Cart cleared successfully.").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // Get cart by user
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return ResponseEntity.ok(cartDto);
    }
}