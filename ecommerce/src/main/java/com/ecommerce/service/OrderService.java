package com.ecommerce.service;

import com.ecommerce.entity.*;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private  CartItemRepository cartItemRepository;
    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private  ProductService productService;

    @Transactional
    public Order createOrderFromCart(User user, Cart cart, OrderStatus status) {

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address latestAddress = addressRepository.findByUser(user)
                .stream()
                .reduce((a, b) -> b)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(latestAddress);
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            Product product = ci.getProduct();

            productService.reduceStock(product, ci.getQuantity());

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(product.getPrice());

            total += product.getPrice() * ci.getQuantity();
            orderItems.add(oi);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);


        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getUserOrdersByStatus(User user, OrderStatus status) {
        return orderRepository.findByUserAndStatus(user, status);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
