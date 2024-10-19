package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
    List<OrderModel> findByUserID(String userID);
    List<OrderModel>findByUser_DisplayNameContainingIgnoreCase(String displayName);
    List<OrderModel> findByOrderID(long orderID);
}
