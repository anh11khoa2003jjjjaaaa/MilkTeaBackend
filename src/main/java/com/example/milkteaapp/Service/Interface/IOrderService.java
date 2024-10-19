package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.DTO.OrderDetailsResponse;
import com.example.milkteaapp.Model.OrderDetailModel;
import com.example.milkteaapp.Model.OrderModel;

import java.util.List;

public interface IOrderService {

    // Lấy tất cả đơn hàng
    List<OrderModel> getAllOrders();

    // Thêm đơn hàng
    OrderModel addOrder(OrderModel orderModel);


    OrderDetailModel addOrderDetail(OrderDetailModel orderDetailModel);
    // Cập nhật đơn hàng
    OrderModel updateOrder(Long orderID, OrderModel updatedOrder);

    List<OrderDetailsResponse> getAllOrdersByCustomerId(String userID);
    // Xóa đơn hàng
    boolean deleteOrder(Long orderID);

    // Tìm kiếm đơn hàng theo CustomerID
    List<OrderModel> searchOrdersByCustomerID(String customerID);
    OrderModel cancelOrder(Long orderID, String cancellationReason);
    OrderModel approveOrder(Long orderID, String statusOrder);
}
