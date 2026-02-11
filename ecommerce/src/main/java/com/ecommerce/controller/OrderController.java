package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/orders")
public class OrderController {

    @Autowired
    private  OrderService orderService;
    @Autowired
    private  UserRepository userRepository;

    @GetMapping
    public String myOrders(
            Authentication authentication,
            @RequestParam(required = false) String status,
            Model model
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Order> orders;

        if (status != null && !status.isEmpty()) {
            OrderStatus st = OrderStatus.valueOf(status);
            orders = orderService.getUserOrdersByStatus(user, st);
        } else {
            orders = orderService.getUserOrders(user);
        }

        long totalOrders = orders.size();

        long pending = orders.stream().filter(o ->
                o.getStatus() == OrderStatus.PAID ||
                        o.getStatus() == OrderStatus.SHIPPED ||
                        o.getStatus() == OrderStatus.OUT_FOR_DELIVERY
        ).count();

        long delivered = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();

        long cancelled = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                .count();

        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pending", pending);
        model.addAttribute("delivered", delivered);
        model.addAttribute("cancelled", cancelled);
        model.addAttribute("selectedStatus", status);

        return "user-orders";
    }
}
