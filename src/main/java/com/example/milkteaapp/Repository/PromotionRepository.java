package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.PromotionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionModel,String> {

    // Tìm kiếm khuyến mãi theo tên
    List<PromotionModel> findByPromotionNameContainingIgnoreCase(String promotionName);
    Optional<PromotionModel> findByPromotionID(String promotionID);
    // Tìm kiếm khuyến mãi theo tỷ lệ giảm giá
    List<PromotionModel> findByDiscountPercentageBetween(BigDecimal minDiscount, BigDecimal maxDiscount);

    // Tìm kiếm khuyến mãi theo khoảng thời gian
    List<PromotionModel> findByStartDateAfterAndEndDateBefore(LocalDate startDate, LocalDate endDate);

//    // Tìm kiếm khuyến mãi đang hoạt động (trong khoảng thời gian hiện tại)
//    List<PromotionModel> findByStartDateBeforeAndEndDateAfter(LocalDate now);
List<PromotionModel> findByStartDateBetween(Date startDate, Date endDate);
}
