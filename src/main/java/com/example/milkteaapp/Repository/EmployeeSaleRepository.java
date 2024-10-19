package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.EmployeeSaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSaleRepository extends JpaRepository<EmployeeSaleModel, Long> {
    // Tìm EmployeeSale theo EmployeeID
    List<EmployeeSaleModel> findByEmployeeID(Long employeeID);

    // Tìm EmployeeSale theo OrderID
    List<EmployeeSaleModel> findByOrderID(Long orderID);
}
