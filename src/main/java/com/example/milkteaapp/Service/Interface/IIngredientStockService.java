package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.IngredientStockModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IIngredientStockService {
    // Lấy tất cả thông tin kho nguyên liệu
    List<IngredientStockModel> getAllStocks();

    // Thêm thông tin kho nguyên liệu
    IngredientStockModel addStock(IngredientStockModel ingredientStockModel);

    // Cập nhật thông tin kho nguyên liệu
    IngredientStockModel updateStock(Long stockID, IngredientStockModel ingredientStockModel);

    // Xóa thông tin kho nguyên liệu
    boolean deleteStock(Long stockID);

    // Tìm kiếm kho nguyên liệu theo tên nguyên liệu
    List<IngredientStockModel> searchStockByIngredientName(String ingredientName);
}
