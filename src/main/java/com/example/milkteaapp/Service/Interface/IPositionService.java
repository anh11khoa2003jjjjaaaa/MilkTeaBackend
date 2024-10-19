package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.PositionModel;

import java.util.List;
import java.util.Map;

public interface IPositionService {

    List<PositionModel> getPositionAll();             // Get all positions

    PositionModel addPosition(PositionModel position);  // Add a new position

    PositionModel updatePosition(String positionID, PositionModel position);  // Update a position

    boolean deletePosition(String positionID);         // Delete a position

    List<PositionModel> searchPosition(String positionID);
    List<Map<String, Object>> getAllPromotionDetailsWithNames();// Search for a position by ID
}
