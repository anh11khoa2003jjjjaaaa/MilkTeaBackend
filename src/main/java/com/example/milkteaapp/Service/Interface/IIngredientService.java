package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.IngredientModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IIngredientService {

    // Thêm Ingredient
    IngredientModel addIngredient(IngredientModel ingredientModel);

    // Sửa Ingredient
    IngredientModel updateIngredient(Long ingredientID, IngredientModel ingredientModel);

    // Xóa Ingredient
    boolean deleteIngredient(Long ingredientID);

    // Tìm kiếm Ingredient theo IngredientID
    IngredientModel findByIngredientID(String ingredientName);

    // Tìm kiếm tất cả Ingredients
    List<IngredientModel> findAllIngredients();
}
