package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.PromotionModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface IPromotionService {
    // Lấy tất cả thông tin khuyến mãi
//    List<PromotionModel> getAllPromotions();
//
//    // Thêm thông tin khuyến mãi
//    PromotionModel addPromotion(PromotionModel promotionModel);
//
//    // Cập nhật thông tin khuyến mãi
//    PromotionModel updatePromotion(String promotionID, PromotionModel promotionModel);
//
//    // Xóa thông tin khuyến mãi
//    boolean deletePromotion(String promotionID);
//
//    // Tìm kiếm khuyến mãi theo tên
//    List<PromotionModel> searchPromotionByName(String promotionName);
    // Create a new promotion
    PromotionModel createPromotion(PromotionModel promotion);

    // Update an existing promotion
    PromotionModel updatePromotion(String promotionID,PromotionModel promotion);

    // Delete a promotion by ID
    boolean deletePromotion(String promotionID);

    // Find a promotion by ID
    PromotionModel getPromotionById(String promotionID);

    // Search promotions by name
    List<PromotionModel> searchPromotionsByName(String name);

    // Search promotions by date range
    List<PromotionModel> searchPromotionsByDateRange(Date startDate, Date endDate);

    // List all promotions
    List<PromotionModel> getAllPromotions();
}
