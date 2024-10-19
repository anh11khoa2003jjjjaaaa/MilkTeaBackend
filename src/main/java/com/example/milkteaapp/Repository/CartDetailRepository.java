package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.CartDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetailModel,Long> {
    Optional<CartDetailModel> findByCartIDAndProduct_ProductNameContainingIgnoreCase(Long cartID, String productName);
    List<CartDetailModel> findByCart_UserID(String userID);
    List<CartDetailModel>findByCartID(Long cartID);
    Optional<CartDetailModel>findByCartIDAndProductID(long cartID, String productID);
}
