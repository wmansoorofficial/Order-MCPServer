package com.order.mcp.service;

import com.order.mcp.models.Item;
import com.order.mcp.models.Order;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderToolService {

    @Autowired
    private EmailService emailService;

    List<Order> orders = new ArrayList<>();

    @Tool(name = "order_get_by_email", description = "Get order details by customer's email address")
    public Order getOrderByEmail(String email) {
        return orders.stream()
                .filter(order -> order.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Tool(name = "order_get_by_orderId", description = "Get order details by customer's order id")
    public Order getOrderByOrderId(int orderId) {
        return orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .orElse(null);
    }

    @Tool(name = "select_item_to_return", description = "Select an item from the order by title to return")
    public Item selectItemToReturn(Order order, String title) {
        return order.getItems().stream()
                .filter(item -> item.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @Tool(name = "choose_refund_method", description = "Choose refund method for the return, either 'original' or 'gift_card'")
    public String chooseRefundMethod(String method) {
        if (method.equalsIgnoreCase("original") || method.equalsIgnoreCase("gift_card")) {
            return method.toLowerCase();
        }
        return "Invalid method. Choose 'original' or 'gift_card'.";
    }

    @Tool(name = "choose_return_method", description = "Choose how the customer wants to return the item: 'store' or 'ups'")
    public String chooseReturnMethod(String method) {
        if (method.equalsIgnoreCase("store") || method.equalsIgnoreCase("ups")) {
            return method.toLowerCase();
        }
        return "Invalid method. Choose 'store' or 'ups'.";
    }

    @Tool(name = "send_return_email", description = "Send return instructions to customer's email")
    public String sendReturnEmail(int orderId, String email, String title, String refundMethod, String returnMethod, String action, String refundAmount) {
        emailService.sendReturnInstructions(orderId, email, title, refundMethod, returnMethod, action, refundAmount);
        return "Email sent to " + email + " with return instructions.";
    }

    @Tool(name = "customer_service_email", description = "Send customer service email")
    public String sendCustomerServiceEmail(String email, String messageBody) {
        emailService.sendEmail(email, messageBody);
        return "Email sent to " + email ;
    }

    @PostConstruct
    public void init() {
        orders.addAll(List.of(
                new Order(1325435435, "wmansoorofficial@gmail.com", "Bronze", "Delivered","07/28/2025",
                        List.of(new Item("35435435345","Logitech C920s Pro HD Webcam", "Normal Sale", "99$")), "Online")
        ));
    }

}
