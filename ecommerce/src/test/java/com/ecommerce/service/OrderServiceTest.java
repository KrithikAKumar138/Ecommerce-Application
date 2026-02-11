package com.ecommerce.service;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.OrderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderFromCart_shouldThrow_whenCartEmpty() {
        Cart cart = new Cart();
        User user = new User();

        when(cartItemRepository.findByCart(cart)).thenReturn(List.of());

        assertThrows(RuntimeException.class, () ->
                orderService.createOrderFromCart(user, cart, OrderStatus.PAID)
        );
    }
}

