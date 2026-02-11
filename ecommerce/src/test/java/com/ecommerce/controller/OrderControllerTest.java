package com.ecommerce.controller;


import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserRepository userRepository;

    private User mockUser() {
        User u = new User();
        u.setId(1L);
        u.setEmail("test@test.com");
        return u;
    }

    @WithMockUser(username = "test@test.com", roles = "USER")
    @Test
    void myOrders_shouldReturnAllOrders_whenNoStatusFilter() throws Exception {
        User user = mockUser();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(orderService.getUserOrders(user)).thenReturn(List.of());

        mockMvc.perform(get("/user/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @WithMockUser(username = "test@test.com", roles = "USER")
    @Test
    void myOrders_shouldReturnFilteredOrders_whenStatusProvided() throws Exception {
        User user = mockUser();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(orderService.getUserOrdersByStatus(user, OrderStatus.DELIVERED))
                .thenReturn(List.of());

        mockMvc.perform(get("/user/orders").param("status", "DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-orders"))
                .andExpect(model().attributeExists("orders"));
    }
}

