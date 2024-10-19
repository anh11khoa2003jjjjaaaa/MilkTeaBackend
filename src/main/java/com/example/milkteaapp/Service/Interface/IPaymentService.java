package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.PaymentModel;

import java.util.List;

public interface IPaymentService {
    List<PaymentModel> getAllPayments();
    PaymentModel getPaymentById(Long paymentID);
    PaymentModel createPayment(PaymentModel paymentModel);
    PaymentModel updatePayment(Long paymentID, PaymentModel updatedPaymentModel);
    boolean deletePayment(Long paymentID);

}
