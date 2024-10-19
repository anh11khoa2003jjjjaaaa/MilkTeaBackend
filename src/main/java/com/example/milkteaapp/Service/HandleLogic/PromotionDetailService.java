package com.example.milkteaapp.Service.HandleLogic;



import com.example.milkteaapp.Model.PromotionDetailModel;
import com.example.milkteaapp.Model.ProductModel;
import com.example.milkteaapp.Repository.PromotionDetailRepository;
import com.example.milkteaapp.Repository.ProductRepository;
import com.example.milkteaapp.Service.Interface.IPromotionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionDetailService implements IPromotionDetailService {

    @Autowired
    private PromotionDetailRepository promotionDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public PromotionDetailModel addPromotionDetail(PromotionDetailModel promotionDetail) {
        return promotionDetailRepository.save(promotionDetail);
    }

    @Override
    public PromotionDetailModel updatePromotionDetail(String promotionDetailID, PromotionDetailModel promotionDetail) {
        Optional<PromotionDetailModel> existingPromotionDetail = promotionDetailRepository.findById(promotionDetailID);
        if (existingPromotionDetail.isPresent()) {
            PromotionDetailModel updatedDetail = existingPromotionDetail.get();
            updatedDetail.setPromotionID(promotionDetail.getPromotionID());
            updatedDetail.setProductID(promotionDetail.getProductID());
            return promotionDetailRepository.save(updatedDetail);
        }
        return null;
    }

    @Override
    public boolean deletePromotionDetail(String promotionDetailID) {
        promotionDetailRepository.deleteById(promotionDetailID);
        return true;
    }

    @Override
    public PromotionDetailModel getPromotionDetailById(String promotionDetailID) {
        return promotionDetailRepository.findById(promotionDetailID).orElse(null);
    }

    @Override
    public List<PromotionDetailModel> findByProductName(String productName) {
        List<ProductModel> products = productRepository.findByProductNameContainingIgnoreCase(productName);
        return promotionDetailRepository.findByProductIn(products);
    }

    @Override
    public List<PromotionDetailModel> getAllPromotionDetails() {
        return promotionDetailRepository.findAll();
    }
}
