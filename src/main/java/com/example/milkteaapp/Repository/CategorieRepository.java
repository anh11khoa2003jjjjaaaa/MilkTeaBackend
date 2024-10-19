package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.CategorieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<CategorieModel, String> {
    List<CategorieModel> findByCategoryNameContainingIgnoreCase(String categoryName);
    CategorieModel findByCategoryID(String categoryID);
    Optional<CategorieModel> findByCategoryNameIgnoreCase(String categoryName);
}
