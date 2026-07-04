package com.akshay.electronic.store.ElectronicStore.services.impl;

import com.akshay.electronic.store.ElectronicStore.dtos.AddItemToCartRequest;
import com.akshay.electronic.store.ElectronicStore.dtos.CartDto;
import com.akshay.electronic.store.ElectronicStore.dtos.UpdateCartItemRequest;
import com.akshay.electronic.store.ElectronicStore.entities.Cart;
import com.akshay.electronic.store.ElectronicStore.entities.CartItem;
import com.akshay.electronic.store.ElectronicStore.entities.Product;
import com.akshay.electronic.store.ElectronicStore.entities.User;
import com.akshay.electronic.store.ElectronicStore.exception.BadApiRequest;
import com.akshay.electronic.store.ElectronicStore.exception.ResourceNotFoundException;
import com.akshay.electronic.store.ElectronicStore.repositories.CartItemRepository;
import com.akshay.electronic.store.ElectronicStore.repositories.CartRepository;
import com.akshay.electronic.store.ElectronicStore.repositories.ProductRepository;
import com.akshay.electronic.store.ElectronicStore.repositories.UserRepository;
import com.akshay.electronic.store.ElectronicStore.services.CartService;
import com.akshay.electronic.store.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

        if (request.getQuantity() <= 0) {
            throw new BadApiRequest("Quantity must be greater than 0");
        }

        if (request.getQuantity() > product.getQuantity()) {
            throw new BadApiRequest("Only " + product.getQuantity() + " items available in stock");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCartId(UUID.randomUUID().toString());
            newCart.setCreatedDate(LocalDateTime.now());
            newCart.setItems(new ArrayList<>());
            newCart.setUser(user);
            return newCart;
        });

        boolean updated = false;

        for (CartItem item : cart.getItems()) {

            if (item.getProduct().getProductId().equals(product.getProductId())) {

                int updatedQuantity = item.getQuantity() + request.getQuantity();

                if (updatedQuantity > product.getQuantity()) {
                    throw new BadApiRequest("Only " + product.getQuantity() + " items available in stock");
                }

                item.setQuantity(updatedQuantity);

                item.setTotalPrice(updatedQuantity * product.getDiscountedPrice());

                updated = true;
                break;
            }
        }

        if (!updated) {

            CartItem newItem = CartItem.builder().product(product).quantity(request.getQuantity()).totalPrice(request.getQuantity() * product.getDiscountedPrice()).cart(cart).build();

            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);

        return mapper.map(savedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found"));

        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new ResourceNotFoundException("Cart Item does not belong to this user");
        }

        cart.getItems().remove(item);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        return mapper.map(cart, CartDto.class);
    }
    @Override
    public void updateCartItemQuantity(String userId, Integer cartItemId, UpdateCartItemRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found"));

        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new BadApiRequest("Cart Item does not belong to this user");
        }

        Product product = item.getProduct();

        if (request.getQuantity() > product.getQuantity()) {
            throw new BadApiRequest("Only " + product.getQuantity() + " items available in stock");
        }
        item.setQuantity(request.getQuantity());

        item.setTotalPrice(request.getQuantity() * product.getDiscountedPrice());

        cartItemRepository.save(item);
    }
}