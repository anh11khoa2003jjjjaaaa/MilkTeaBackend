package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.OrderDetailModel;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetailModel> getAllOrderDetails();
    List<OrderDetailModel> getOrderDetailsByOrderID(Long orderID);
    OrderDetailModel getOrderDetailById(Long orderDetailID);
    OrderDetailModel createOrderDetail(OrderDetailModel orderDetailModel);
    OrderDetailModel updateOrderDetail(Long orderDetailID, OrderDetailModel updatedOrderDetail);
    boolean deleteOrderDetail(Long orderDetailID);
}
