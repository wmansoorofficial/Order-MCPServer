package com.order.mcp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item {

    String upc;
    String title;
    String saleType;
    String unitPrice;
}
