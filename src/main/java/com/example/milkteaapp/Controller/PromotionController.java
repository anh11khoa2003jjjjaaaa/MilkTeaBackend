
package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.PromotionModel;
import com.example.milkteaapp.Service.HandleLogic.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/promotions")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"})
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    // Thêm mới khuyến mãi
    @PostMapping("/add")
    public ResponseEntity<?> addPromotion(@RequestBody PromotionModel promotion) {
        try {
            PromotionModel savedPromotion = promotionService.createPromotion(promotion);
            return new ResponseEntity<>(savedPromotion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Dữ liệu khuyến mãi không hợp lệ: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi tạo khuyến mãi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật khuyến mãi
    @PutMapping("/update/{promotionID}")
    public ResponseEntity<?> updatePromotion(@PathVariable String promotionID, @RequestBody PromotionModel promotion) {
        try {
            PromotionModel updatedPromotion = promotionService.updatePromotion(promotionID, promotion);
            return new ResponseEntity<>(updatedPromotion, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Dữ liệu khuyến mãi không hợp lệ: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật khuyến mãi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa khuyến mãi theo ID
    @DeleteMapping("/delete/{promotionID}")
    public ResponseEntity<?> deletePromotion(@PathVariable String promotionID) {
        try {
            promotionService.deletePromotion(promotionID);
            return new ResponseEntity<>("Xóa khuyến mãi thành công", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Không tìm thấy khuyến mãi với ID: " + promotionID, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi xóa khuyến mãi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy khuyến mãi theo ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable String id) {
        try {
            PromotionModel promotion = promotionService.getPromotionById(id);
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Không tìm thấy khuyến mãi với ID: " + id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi lấy khuyến mãi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm khuyến mãi theo tên
    @GetMapping("/search/name")
    public ResponseEntity<?> searchPromotionsByName(@RequestParam String name) {
        try {
            List<PromotionModel> promotions = promotionService.searchPromotionsByName(name);
            return new ResponseEntity<>(promotions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi tìm kiếm khuyến mãi theo tên", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm khuyến mãi theo khoảng thời gian
    @GetMapping("/search/date")
    public ResponseEntity<?> searchPromotionsByDate(@RequestParam Date startDate, @RequestParam Date endDate) {
        try {
            List<PromotionModel> promotions = promotionService.searchPromotionsByDateRange(startDate, endDate);
            return new ResponseEntity<>(promotions, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Khoảng thời gian không hợp lệ: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi tìm kiếm khuyến mãi theo khoảng thời gian", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Liệt kê tất cả các khuyến mãi
    @GetMapping("/all")
    public ResponseEntity<?> getAllPromotions() {
        try {
            List<PromotionModel> promotions = promotionService.getAllPromotions();
            return new ResponseEntity<>(promotions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi lấy danh sách khuyến mãi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all promotion details with promotion name and product name
    @GetMapping("/promotionDetailsWithNames")
    public ResponseEntity<List<Map<String, Object>>> getAllPromotionDetails() {
        List<Map<String, Object>> promotionDetailsWithNames = promotionService.getAllPromotionDetailsWithNames();
        return ResponseEntity.ok(promotionDetailsWithNames);
    }
}
