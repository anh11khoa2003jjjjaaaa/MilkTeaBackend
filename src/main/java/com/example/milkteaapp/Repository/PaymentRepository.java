package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel,Long> {
}
