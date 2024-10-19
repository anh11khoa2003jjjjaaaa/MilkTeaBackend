package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierModel, String> {
}
