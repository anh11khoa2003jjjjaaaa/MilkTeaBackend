//package com.example.milkteaapp.Service.HandleLogic;
//
//import com.example.milkteaapp.Model.PositionModel;
//import com.example.milkteaapp.Repository.PositionRepository;
//import com.example.milkteaapp.Service.Interface.IPositionService;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class PositionService implements IPositionService {
//    @Autowired
//    private PositionRepository positionRepository;
//
//    // Fetch all positions
//    @Override
//    public List<PositionModel> getPositionAll() {
//        return positionRepository.findAll();
//    }
//
//    // Add a new position
//    @Override
//    public PositionModel addPosition(PositionModel position) {
//        // Generate a new position ID
//        String newPositionId = generateNewPositionId();
//
//        // Check if the position ID or name already exists
//        Optional<PositionModel> existingPosition = positionRepository.findById(newPositionId);
//        Optional<PositionModel> existingPositionName = positionRepository.findByPositionNameIgnoreCase(position.getPositionName());
//
//        if (existingPosition.isPresent() || existingPositionName.isPresent()) {
//            throw new IllegalStateException("Position name: " + position.getPositionName() + " or position ID: " + newPositionId + " already exists.");
//        }
//
//        // Set the generated position ID
//        position.setPositionID(newPositionId);
//        return positionRepository.save(position);
//    }
//
//    // Generate a new unique position ID
//    private String generateNewPositionId() {
//        List<PositionModel> positions = positionRepository.findAll();
//
//        // Get the highest current position ID starting with "POS" (e.g., "POS001")
//        String maxId = positions.stream()
//                .map(PositionModel::getPositionID)
//                .filter(id -> id.startsWith("POS"))
//                .sorted()
//                .reduce((first, second) -> second)
//                .orElse("POS001"); // Default starting ID if none exist
//
//        // Increment the position ID number
//        int newIdNumber = Integer.parseInt(maxId.substring(3)) + 1;
//        return String.format("POS%03d", newIdNumber);
//    }
//
//    // Update a position
//    @Override
//    public PositionModel updatePosition(String positionID, PositionModel position) {
//        Optional<PositionModel> optionalPosition = positionRepository.findById(positionID);
//
//        if (optionalPosition.isPresent()) {
//            PositionModel existingPosition = optionalPosition.get();
//            updatePositionDetail(existingPosition, position);
//
//            return positionRepository.save(existingPosition);
//        } else {
//            throw new EntityNotFoundException("Position with ID " + positionID + " not found.");
//        }
//    }
//
//    // Update position details
//    private void updatePositionDetail(PositionModel existingPosition, PositionModel newPosition) {
//        existingPosition.setPositionName(newPosition.getPositionName());
//    }
//
//    // Delete a position
//    @Override
//    public boolean deletePosition(String positionID) {
//        PositionModel positionModel = positionRepository.findByPositionID(positionID);
//
//        if (positionModel != null) {
//            positionRepository.deleteById(positionID);
//            return true;
//        }
//        return false;
//    }
//
//    // Search positions by name or ID
//    @Override
//    public List<PositionModel> searchPosition(String positionName) {
//        if (positionName == null || positionName.isEmpty()) {
//            return positionRepository.findAll();
//        } else {
//            List<PositionModel> positions = positionRepository.findByPositionNameContainingIgnoreCase(positionName);
//            if (positions.isEmpty()) {
//                throw new EntityNotFoundException("No positions found with name " + positionName);
//            }
//            return positions;
//        }
//    }
//}

package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.PromotionDetailModel;
import com.example.milkteaapp.Model.PromotionModel;
import com.example.milkteaapp.Repository.PromotionDetailRepository;
import com.example.milkteaapp.Repository.PromotionRepository;
import com.example.milkteaapp.Service.Interface.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromotionService implements IPromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionDetailRepository promotionDetailRepository;

    // Create a new promotion
    @Override
    public PromotionModel createPromotion(PromotionModel promotion) {
        return promotionRepository.save(promotion);
    }

    // Update an existing promotion
    @Override
    public PromotionModel updatePromotion(String promotionID,PromotionModel promotion) {
        if (promotionRepository.existsById(promotion.getPromotionID())) {
            return promotionRepository.save(promotion);
        } else {
            throw new RuntimeException("Promotion not found");
        }
    }

    // Delete a promotion by ID
    @Override
    public boolean deletePromotion(String promotionID) {
        if (promotionRepository.existsById(promotionID)) {
            promotionRepository.deleteById(promotionID);
        } else {
            throw new RuntimeException("Promotion not found");
        }
        return true;
    }

    // Find a promotion by ID
    @Override
    public PromotionModel getPromotionById(String promotionID) {
        return promotionRepository.findById(promotionID)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    // Search promotions by name
    @Override
    public List<PromotionModel> searchPromotionsByName(String name) {
        return promotionRepository.findByPromotionNameContainingIgnoreCase(name);
    }

    // Search promotions by date range
    @Override
    public List<PromotionModel> searchPromotionsByDateRange(Date startDate, Date endDate) {
        return promotionRepository.findByStartDateBetween(startDate, endDate);
    }

    // List all promotions
    @Override
    public List<PromotionModel> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public List<Map<String, Object>> getAllPromotionDetailsWithNames() {
        List<PromotionDetailModel> promotionDetails = promotionDetailRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();

        for (PromotionDetailModel detail : promotionDetails) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("promotionDetailID", detail.getPromotionDetailID());
            detailMap.put("promotionName", detail.getPromotion().getPromotionName()); // Lấy tên khuyến mãi
            detailMap.put("productName", detail.getProduct().getProductName()); // Lấy tên sản phẩm
            results.add(detailMap);
        }
        return results;
    }
}
