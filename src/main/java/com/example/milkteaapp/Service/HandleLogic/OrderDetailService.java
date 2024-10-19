package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.OrderDetailModel;
import com.example.milkteaapp.Repository.OrderDetailRepository;
import com.example.milkteaapp.Service.Interface.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetailModel> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetailModel> getOrderDetailsByOrderID(Long orderID) {
        return orderDetailRepository.findByOrderID(orderID);
    }

    @Override
    public OrderDetailModel getOrderDetailById(Long orderDetailID) {
        return orderDetailRepository.findById(orderDetailID)
                .orElseThrow(() -> new NoSuchElementException("OrderDetail not found with id: " + orderDetailID));
    }

    @Override
    public OrderDetailModel createOrderDetail(OrderDetailModel orderDetailModel) {
        // Bẫy lỗi nếu dữ liệu đầu vào không hợp lệ
        if (orderDetailModel.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (orderDetailModel.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        return orderDetailRepository.save(orderDetailModel);
    }

    @Override
    public OrderDetailModel updateOrderDetail(Long orderDetailID, OrderDetailModel updatedOrderDetail) {
        OrderDetailModel existingOrderDetail = orderDetailRepository.findById(orderDetailID)
                .orElseThrow(() -> new NoSuchElementException("OrderDetail not found with id: " + orderDetailID));

        // Cập nhật các trường cần thiết
        existingOrderDetail.setOrderID(updatedOrderDetail.getOrderID());
        existingOrderDetail.setProductID(updatedOrderDetail.getProductID());
        existingOrderDetail.setQuantity(updatedOrderDetail.getQuantity());
        existingOrderDetail.setSize(updatedOrderDetail.getSize());
        existingOrderDetail.setPrice(updatedOrderDetail.getPrice());

        // Lưu lại các thay đổi
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public boolean deleteOrderDetail(Long orderDetailID) {
        if (!orderDetailRepository.existsById(orderDetailID)) {
            throw new NoSuchElementException("OrderDetail not found with id: " + orderDetailID);
        }
        orderDetailRepository.deleteById(orderDetailID);
        return true;
    }
}
