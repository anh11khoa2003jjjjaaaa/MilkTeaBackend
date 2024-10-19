package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeModel,Long> {
    List<EmployeeModel> findByEmployeeNameContainingIgnoreCase(String employeeName);
}
