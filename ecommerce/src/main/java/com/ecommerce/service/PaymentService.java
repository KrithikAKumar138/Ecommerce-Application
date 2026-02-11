package com.ecommerce.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.json.JSONObject;


@Service
public class PaymentService {

    @Autowired
    private  RazorpayService razorpayService;


    public JSONObject createPaymentOrder(double amount) throws Exception {
        return razorpayService.createOrder(amount);
    }

    public boolean verifyPayment(Map<String, String> payload) {
        return true;
    }
}
