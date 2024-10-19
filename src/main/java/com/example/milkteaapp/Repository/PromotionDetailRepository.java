package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Model.PromotionDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetailModel,String> {
    List<PromotionDetailModel> findByProductIn(List<ProductModel> products);

}
