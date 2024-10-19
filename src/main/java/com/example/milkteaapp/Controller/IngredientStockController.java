package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.IngredientStockModel;
import com.example.milkteaapp.Service.Interface.IIngredientStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/ingredient-stocks")
public class IngredientStockController {

    private final IIngredientStockService ingredientStockService;

    public IngredientStockController(IIngredientStockService ingredientStockService) {
        this.ingredientStockService = ingredientStockService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientStockModel>> getAllStocks() {
        List<IngredientStockModel> stocks = ingredientStockService.getAllStocks();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IngredientStockModel> addStock(@RequestBody IngredientStockModel ingredientStockModel) {
        IngredientStockModel newStock = ingredientStockService.addStock(ingredientStockModel);
        return new ResponseEntity<>(newStock, HttpStatus.CREATED);
    }

    @PutMapping("/{stockID}")
    public ResponseEntity<IngredientStockModel> updateStock(
            @PathVariable Long stockID,
            @RequestBody IngredientStockModel ingredientStockModel) {
        IngredientStockModel updatedStock = ingredientStockService.updateStock(stockID, ingredientStockModel);
        return new ResponseEntity<>(updatedStock, HttpStatus.OK);
    }

    @DeleteMapping("/{stockID}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long stockID) {
        ingredientStockService.deleteStock(stockID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<IngredientStockModel>> searchStockByIngredientName(@RequestParam String ingredientName) {
        List<IngredientStockModel> stocks = ingredientStockService.searchStockByIngredientName(ingredientName);
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }
}
