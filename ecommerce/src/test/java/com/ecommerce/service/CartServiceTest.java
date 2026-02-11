package com.ecommerce.service;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addToCart_shouldAddNewItem_whenNotExists() {
        User user = new User();
        user.setEmail("test@test.com");

        Cart cart = new Cart();
        cart.setUser(user);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());

        cartService.addToCart("test@test.com", 1L);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addToCart_shouldIncreaseQuantity_whenItemExists() {
        User user = new User();
        user.setEmail("test@test.com");

        Cart cart = new Cart();
        cart.setUser(user);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        CartItem existingItem = new CartItem();
        existingItem.setQuantity(1);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(existingItem));

        cartService.addToCart("test@test.com", 1L);

        assertEquals(2, existingItem.getQuantity());
        verify(cartItemRepository).save(existingItem);
    }
}
