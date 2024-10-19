package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.IngredientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientModel,Long> {
    Optional<IngredientModel> findByIngredientName(String ingredientName);
}
