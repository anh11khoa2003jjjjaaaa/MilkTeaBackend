package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.CustomerReviewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICustomerReviewService {
    // Lấy tất cả đánh giá của khách hàng
    List<CustomerReviewModel> getAllReviews();

    // Thêm mới đánh giá của khách hàng
    CustomerReviewModel addReview(CustomerReviewModel customerReviewModel);

    // Cập nhật đánh giá của khách hàng
    CustomerReviewModel updateReview(long reviewID, CustomerReviewModel customerReviewModel);

    // Xóa đánh giá của khách hàng
    boolean deleteReview(long reviewID);

    // Tìm kiếm đánh giá theo tên khách hàng
    List<CustomerReviewModel> searchReviewByCustomerName(String displayName);
}
