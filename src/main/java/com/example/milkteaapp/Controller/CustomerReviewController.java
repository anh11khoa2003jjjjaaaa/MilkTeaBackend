package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.CustomerReviewModel;
import com.example.milkteaapp.Service.HandleLogic.CustomerReViewService;
import com.example.milkteaapp.Service.Interface.ICustomerReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/customer_reviews")
public class CustomerReviewController {

    private final ICustomerReviewService customerReviewService;

    public CustomerReviewController(CustomerReViewService customerReviewService) {
        this.customerReviewService = customerReviewService;
    }

    // Lấy tất cả các đánh giá
    @GetMapping("/all")
    public ResponseEntity<List<CustomerReviewModel>> getAllReviews() {
        List<CustomerReviewModel> reviews = customerReviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Thêm mới một đánh giá
    @PostMapping("/add")
    public ResponseEntity<CustomerReviewModel> addReview(@RequestBody CustomerReviewModel customerReviewModel) {
        CustomerReviewModel newReview = customerReviewService.addReview(customerReviewModel);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    // Cập nhật một đánh giá
    @PutMapping("/update/{reviewID}")
    public ResponseEntity<CustomerReviewModel> updateReview(@PathVariable long reviewID, @RequestBody CustomerReviewModel customerReviewModel) {
        CustomerReviewModel updatedReview = customerReviewService.updateReview(reviewID, customerReviewModel);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    // Xóa một đánh giá
    @DeleteMapping("/delete/{reviewID}")
    public ResponseEntity<String> deleteReview(@PathVariable long reviewID) {
        boolean isDeleted = customerReviewService.deleteReview(reviewID);
        if (isDeleted) {
            return new ResponseEntity<>("Đánh giá đã được xóa thành công!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Xóa đánh giá thất bại!", HttpStatus.NOT_FOUND);
        }
    }

    // Tìm kiếm đánh giá theo tên khách hàng
    @GetMapping("/search")
    public ResponseEntity<List<CustomerReviewModel>> searchReviewByCustomerName(@RequestParam String displayName) {
        List<CustomerReviewModel> reviews = customerReviewService.searchReviewByCustomerName(displayName);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
