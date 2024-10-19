package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.OrderDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailModel,Long> {
    List<OrderDetailModel> findByOrderID(Long orderID);
    Optional<OrderDetailModel> findById(Long orderDetailID);
}
