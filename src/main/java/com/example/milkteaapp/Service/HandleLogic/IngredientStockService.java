package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.IngredientStockModel;
import com.example.milkteaapp.Repository.IngredientStockRepository;
import com.example.milkteaapp.Service.Interface.IIngredientStockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientStockService implements IIngredientStockService {

    private final IngredientStockRepository ingredientStockRepository;

    public IngredientStockService(IngredientStockRepository ingredientStockRepository) {
        this.ingredientStockRepository = ingredientStockRepository;
    }

    @Override
    public List<IngredientStockModel> getAllStocks() {
        return ingredientStockRepository.findAll();
    }

    @Override
    public IngredientStockModel addStock(IngredientStockModel ingredientStockModel) {
        return ingredientStockRepository.save(ingredientStockModel);
    }

    @Override
    public IngredientStockModel updateStock(Long stockID, IngredientStockModel ingredientStockModel) {
        Optional<IngredientStockModel> existingStock = ingredientStockRepository.findById(stockID);
        if (existingStock.isPresent()) {
            IngredientStockModel stock = existingStock.get();
            updateStockInfo(ingredientStockModel, stock);
            return ingredientStockRepository.save(stock);
        } else {
            throw new EntityNotFoundException("Không tìm thấy kho nguyên liệu với ID " + stockID);
        }
    }

    private void updateStockInfo(IngredientStockModel source, IngredientStockModel target) {
        target.setIngredientID(source.getIngredientID());
        target.setQuantity(source.getQuantity());
        target.setStockDate(source.getStockDate());
    }

    @Override
    public boolean deleteStock(Long stockID) {
        Optional<IngredientStockModel> stock = ingredientStockRepository.findById(stockID);
        if (stock.isPresent()) {
            ingredientStockRepository.deleteById(stockID);
            return true;
        } else {
            throw new EntityNotFoundException("Không tìm thấy kho nguyên liệu để xóa với ID " + stockID);
        }
    }

    @Override
    public List<IngredientStockModel> searchStockByIngredientName(String ingredientName) {
        return ingredientStockRepository.findByIngredient_IngredientNameContainingIgnoreCase(ingredientName);
    }
}
