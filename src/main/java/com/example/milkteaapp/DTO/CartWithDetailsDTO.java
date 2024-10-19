package com.example.milkteaapp.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CartWithDetailsDTO  {
    private Long cartID;
    private String userID;
    private Date createdDate;

    // Thông tin từ CartDetail
    private Long cartDetailID;
    private Long productID;
    private int quantity;
    private BigDecimal price;
    private String size;
    private String image;



}
