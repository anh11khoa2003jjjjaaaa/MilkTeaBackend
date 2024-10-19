package com.example.milkteaapp.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDTO {
    private Long orderDetailID;
    private Long orderID;
    private String productID;
    private Integer quantity;
    private String size;
    private BigDecimal price;
    private String productName;
}
