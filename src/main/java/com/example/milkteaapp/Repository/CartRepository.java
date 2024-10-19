package com.example.milkteaapp.Repository;


import com.example.milkteaapp.DTO.CartWithDetailsDTO;
import com.example.milkteaapp.Model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartModel,Long> {
    List<CartModel> findByCreatedDate(Date createdDate);
    List<CartModel> findByUserID(String userID);










}
