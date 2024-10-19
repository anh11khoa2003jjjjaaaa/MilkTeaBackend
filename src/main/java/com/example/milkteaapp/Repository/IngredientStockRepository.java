package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.IngredientStockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientStockRepository extends JpaRepository<IngredientStockModel, Long> {
    List<IngredientStockModel> findByIngredient_IngredientNameContainingIgnoreCase(String ingredientName);
}
