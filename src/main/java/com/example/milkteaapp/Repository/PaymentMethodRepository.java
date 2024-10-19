package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.PaymentMethodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository  extends JpaRepository<PaymentMethodModel, Long> {
    List<PaymentMethodModel> findByMethodNameContainingIgnoreCase(String paymentMethodName);
    PaymentMethodModel findByPaymentMethodID(long paymentMethodID);
}
