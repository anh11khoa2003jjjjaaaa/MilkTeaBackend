package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.PositionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionModel, String> {
    Optional<PositionModel> findByPositionNameIgnoreCase(String position);
    PositionModel findByPositionID(String positionID);
    List<PositionModel> findByPositionNameContainingIgnoreCase(String position);
}
