package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.service.CheckoutService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.service.RazorpayService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private  CheckoutService checkoutService;
    @Autowired
    private  PaymentService paymentService;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RazorpayService razorpayService;


    @GetMapping
    public String checkoutPage(Authentication authentication, Model model) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Cart cart = checkoutService.getCartForUser(user);

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart?empty";
        }

        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotalPrice());

        return "checkout";
    }

    @PostMapping("/confirm")
    public String confirmPage(Authentication authentication,
                              @RequestParam String name,
                              @RequestParam String address,
                              @RequestParam String phone,
                              Model model) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        var savedAddress = checkoutService.saveAddress(user, name, address, phone);

        Cart cart = checkoutService.getCartForUser(user);

        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotalPrice());
        model.addAttribute("address", savedAddress);

        return "checkout-confirm";
    }

    @PostMapping("/create-payment-order")
    @ResponseBody
    public Map<String, Object> createPaymentOrder(Authentication authentication) throws Exception {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Cart cart = checkoutService.getCartForUser(user);

        JSONObject razorpayOrder = paymentService.createPaymentOrder(cart.getTotalPrice());

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", razorpayOrder.get("id"));
        response.put("amount", razorpayOrder.get("amount"));
        response.put("currency", "INR");

        // ✅ THIS IS THE IMPORTANT LINE
        response.put("key", razorpayService.getKeyId());

        return response;
    }


    @PostMapping("/verify-payment")
    @ResponseBody
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload,
                                           Authentication authentication) {

        boolean success = paymentService.verifyPayment(payload);

        if (!success) {
            return ResponseEntity.badRequest().body("Payment verification failed");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Cart cart = checkoutService.getCartForUser(user);

        checkoutService.placeOrder(user, cart);

        return ResponseEntity.ok("Payment successful & order placed");
    }
}
