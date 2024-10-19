package com.example.milkteaapp.DTO;

import com.example.milkteaapp.Model.OrderDetailModel;
import com.example.milkteaapp.Model.OrderModel;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailsResponse {

    private List<OrderDetailModel> orderDetails;
    private List<OrderModel>ordermodels;
    private BigDecimal totalAmount;

    public OrderDetailsResponse(List<OrderDetailModel> orderDetails,List<OrderModel>ordermodels, BigDecimal totalAmount) {
        this.orderDetails = orderDetails;
        this.totalAmount = totalAmount;
        this.ordermodels = ordermodels;
    }

    public List<OrderDetailModel> getOrderDetails() {
        return orderDetails;
    }
    public List<OrderModel> getOrdermodels() {
        return ordermodels;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
