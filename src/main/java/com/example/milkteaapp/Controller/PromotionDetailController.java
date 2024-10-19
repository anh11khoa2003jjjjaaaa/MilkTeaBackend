package com.example.milkteaapp.Controller;



import com.example.milkteaapp.Model.PromotionDetailModel;
import com.example.milkteaapp.Service.HandleLogic.PromotionDetailService;
import com.example.milkteaapp.Service.HandleLogic.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/promotiondetails")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"})
public class PromotionDetailController {

    @Autowired
    private PromotionDetailService promotionDetailService;

    // Thêm PromotionDetail
    @PostMapping("/add")
    public ResponseEntity<PromotionDetailModel> addPromotionDetail(@RequestBody PromotionDetailModel promotionDetail) {
        PromotionDetailModel newPromotionDetail = promotionDetailService.addPromotionDetail(promotionDetail);
        return ResponseEntity.ok(newPromotionDetail);
    }

    // Cập nhật PromotionDetail
    @PutMapping("/update/{id}")
    public ResponseEntity<PromotionDetailModel> updatePromotionDetail(
            @PathVariable("id") String promotionDetailID,
            @RequestBody PromotionDetailModel promotionDetail) {
        PromotionDetailModel updatedPromotionDetail = promotionDetailService.updatePromotionDetail(promotionDetailID, promotionDetail);
        if (updatedPromotionDetail != null) {
            return ResponseEntity.ok(updatedPromotionDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa PromotionDetail
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePromotionDetail(@PathVariable("id") String promotionDetailID) {
        promotionDetailService.deletePromotionDetail(promotionDetailID);
        return ResponseEntity.noContent().build();
    }

    // Tìm kiếm theo tên sản phẩm
    @GetMapping("/search")
    public ResponseEntity<List<PromotionDetailModel>> findByProductName(@RequestParam("productName") String productName) {
        List<PromotionDetailModel> promotionDetails = promotionDetailService.findByProductName(productName);
        return ResponseEntity.ok(promotionDetails);
    }

    // Lấy tất cả PromotionDetails
    @GetMapping("/all")
    public ResponseEntity<List<PromotionDetailModel>> getAllPromotionDetails() {
        List<PromotionDetailModel> promotionDetails = promotionDetailService.getAllPromotionDetails();
        return ResponseEntity.ok(promotionDetails);
    }



}
