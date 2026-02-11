package com.ecommerce.service;

import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private  CartRepository cartRepository;
    @Autowired
    private  CartItemRepository cartItemRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  UserRepository userRepository;


    public Cart getCartByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    public void addToCart(String email, Long productId) {
        Cart cart = getCartByUserEmail(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository
                .findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(1);
            cartItemRepository.save(item);
        }
    }


    public void updateQuantity(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }
    public void clearCartByEmail(String email) {
        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }


    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }
}
