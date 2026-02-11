package com.ecommerce.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;

import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.AddressRepository;


@Service
public class CheckoutService {

    @Autowired
    private  CartRepository cartRepository;
    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private  OrderService orderService;

    public Cart getCartForUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Address saveAddress(User user, String name, String address, String phone) {
        Address addr = new Address();
        addr.setUser(user);
        addr.setName(name);
        addr.setAddress(address);
        addr.setPhone(phone);
        return addressRepository.save(addr);
    }

    @Transactional
    public Order placeOrder(User user, Cart cart) {
        return orderService.createOrderFromCart(user, cart, OrderStatus.PAID);
    }
}
