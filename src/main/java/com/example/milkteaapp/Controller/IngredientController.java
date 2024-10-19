package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.IngredientModel;
import com.example.milkteaapp.Service.Interface.IIngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/ingredients")
public class IngredientController {

    private final IIngredientService ingredientService;

    public IngredientController(IIngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("search/{ingredientID}")
    public ResponseEntity<IngredientModel> getIngredientByID(@PathVariable String ingredientName) {
        IngredientModel ingredient = ingredientService.findByIngredientID(ingredientName);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/all")
    public ResponseEntity<List<IngredientModel>> getAllIngredients() {
        List<IngredientModel> ingredients = ingredientService.findAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/add")
    public ResponseEntity<IngredientModel> addIngredient(@RequestBody IngredientModel ingredientModel) {
        IngredientModel newIngredient = ingredientService.addIngredient(ingredientModel);
        return ResponseEntity.ok(newIngredient);
    }

    @PutMapping("/update/{ingredientID}")
    public ResponseEntity<IngredientModel> updateIngredient(@PathVariable Long ingredientID, @RequestBody IngredientModel ingredientModel) {
        IngredientModel updatedIngredient = ingredientService.updateIngredient(ingredientID, ingredientModel);
        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/delete/{ingredientID}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientID) {
        boolean isDeleted = ingredientService.deleteIngredient(ingredientID);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
