package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.CategorieModel;
import com.example.milkteaapp.Model.ProductModel;
import jakarta.validation.constraints.Null;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel,String> {
    List<ProductModel> findByProductName(String productName);
//    List<ProductModel> findByProductName(String productName);
//   ProductModel findByProductID(String productID);
    Optional<ProductModel> findByProductID(String productID);
    List<ProductModel> findByCategoryID(String categoryID);


    Page<ProductModel> findAll(Pageable pageable);

   List<ProductModel> findByProductNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(String productName, String categoryName);
    List<ProductModel>findByProductNameContainingIgnoreCase(String productName);
}
