package com.order.mcp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {

    private int id;
    private String email;
    private String customerLoyaltyTier;
    private String status;
    private String date;
    private List<Item> items;
    private String purchaseChannel;

}
