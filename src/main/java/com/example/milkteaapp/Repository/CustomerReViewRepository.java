package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.CustomerReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerReViewRepository extends JpaRepository<CustomerReviewModel,Long> {
    List<CustomerReviewModel> findByUser_DisplayNameContainingIgnoreCase(String customerName);
}
