package com.example.milkteaapp.Service.Interface;



import com.example.milkteaapp.Model.PromotionDetailModel;
import java.util.List;

public interface IPromotionDetailService {
    // Thêm PromotionDetail mới
    PromotionDetailModel addPromotionDetail(PromotionDetailModel promotionDetail);

    // Cập nhật PromotionDetail
    PromotionDetailModel updatePromotionDetail(String promotionDetailID, PromotionDetailModel promotionDetail);

    // Xóa PromotionDetail theo ID
    boolean deletePromotionDetail(String promotionDetailID);

    // Tìm PromotionDetail theo ID
    PromotionDetailModel getPromotionDetailById(String promotionDetailID);

    // Tìm kiếm PromotionDetail theo tên sản phẩm
    List<PromotionDetailModel> findByProductName(String productName);

    // Lấy tất cả PromotionDetails
    List<PromotionDetailModel> getAllPromotionDetails();
}

