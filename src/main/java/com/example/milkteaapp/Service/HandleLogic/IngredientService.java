package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.IngredientModel;
import com.example.milkteaapp.Repository.IngredientRepository;
import com.example.milkteaapp.Service.Interface.IIngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService implements IIngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientModel addIngredient(IngredientModel ingredientModel) {
        return ingredientRepository.save(ingredientModel);
    }

    @Override
    public IngredientModel updateIngredient(Long ingredientID, IngredientModel ingredientModel) {
        Optional<IngredientModel> existingIngredient = ingredientRepository.findById(ingredientID);
        if (existingIngredient.isPresent()) {
            IngredientModel ingredient = existingIngredient.get();
            updateIngredientInfo(ingredientModel, ingredient);
            return ingredientRepository.save(ingredient);
        } else {
            throw new EntityNotFoundException("Không tìm thấy nguyên liệu với ID " + ingredientID);
        }
    }

    private void updateIngredientInfo(IngredientModel source, IngredientModel target) {
        target.setIngredientName(source.getIngredientName());
        target.setPrice(source.getPrice());
        target.setSupplierID(source.getSupplierID());
    }

    @Override
    public boolean deleteIngredient(Long ingredientID) {
        Optional<IngredientModel> ingredient = ingredientRepository.findById(ingredientID);
        if (ingredient.isPresent()) {
            ingredientRepository.deleteById(ingredientID);
            return true;
        } else {
            throw new EntityNotFoundException("Không tìm thấy nguyên liệu với ID " + ingredientID);
        }
    }

    @Override
    public IngredientModel findByIngredientID(String ingredientName) {
        return ingredientRepository.findByIngredientName(ingredientName)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nguyên liệu với ID " + ingredientName));
    }

    @Override
    public List<IngredientModel> findAllIngredients() {
        return ingredientRepository.findAll();
    }
}
