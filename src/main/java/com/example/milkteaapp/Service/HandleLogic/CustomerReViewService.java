package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.CustomerReviewModel;
import com.example.milkteaapp.Repository.CustomerReViewRepository;
import com.example.milkteaapp.Service.Interface.ICustomerReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerReViewService implements ICustomerReviewService {
    private final CustomerReViewRepository customerReviewRepository;

    public CustomerReViewService(CustomerReViewRepository customerReviewRepository) {
        this.customerReviewRepository = customerReviewRepository;
    }

    @Override
    public List<CustomerReviewModel> getAllReviews() {
        return customerReviewRepository.findAll();
    }

    @Override
    public CustomerReviewModel addReview(CustomerReviewModel customerReviewModel) {
        return customerReviewRepository.save(customerReviewModel);
    }

    @Override
    public CustomerReviewModel updateReview(long reviewID, CustomerReviewModel customerReviewModel) {
        Optional<CustomerReviewModel> existingReview = customerReviewRepository.findById(reviewID);
        if (existingReview.isPresent()) {
            CustomerReviewModel review = existingReview.get();
            updateReviewInfo(customerReviewModel, review);
            return customerReviewRepository.save(review);
        } else {
            throw new EntityNotFoundException("Không tìm thấy đánh giá với ID " + reviewID);
        }
    }

    private void updateReviewInfo(CustomerReviewModel source, CustomerReviewModel target) {
        target.setUserID(source.getUserID());
        target.setProductID(source.getProductID());
        target.setRating(source.getRating());
        target.setComment(source.getComment());
        target.setReviewDate(source.getReviewDate());
    }

    @Override
    public boolean deleteReview(long reviewID) {
        Optional<CustomerReviewModel> review = customerReviewRepository.findById(reviewID);
        if (review.isPresent()) {
            customerReviewRepository.deleteById(reviewID);
            return true;
        } else {
            throw new EntityNotFoundException("Không tìm thấy đánh giá để xóa với ID " + reviewID);
        }
    }

    @Override
    public List<CustomerReviewModel> searchReviewByCustomerName(String displayName) {
        return customerReviewRepository.findByUser_DisplayNameContainingIgnoreCase(displayName);
    }
}

