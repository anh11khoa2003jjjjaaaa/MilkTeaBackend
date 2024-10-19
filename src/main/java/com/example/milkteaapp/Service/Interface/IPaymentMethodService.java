package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.PaymentMethodModel;

import java.util.List;

public interface IPaymentMethodService {
    List<PaymentMethodModel> getAllPaymentMethods();
    List<PaymentMethodModel> search(String paymentMethodName);
    PaymentMethodModel createPaymentMethod(PaymentMethodModel paymentMethodModel);
    PaymentMethodModel updatePaymentMethod(Long paymentMethodID, PaymentMethodModel updatedPaymentMethodModel);
    void deletePaymentMethod(Long paymentMethodID);
    PaymentMethodModel getPaymentMethodById(Long paymentMethodID);
}
