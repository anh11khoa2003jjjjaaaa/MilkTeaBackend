package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.PaymentMethodModel;
import com.example.milkteaapp.Repository.PaymentMethodRepository;
import com.example.milkteaapp.Service.Interface.IPaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentMethodService implements IPaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethodModel> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public List<PaymentMethodModel> search(String methodName) {

        if(methodName==null){

            paymentMethodRepository.findAll();
        }else{

        }
        List<PaymentMethodModel> paymentMethodModels = paymentMethodRepository.findByMethodNameContainingIgnoreCase(methodName);
        if (paymentMethodModels.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy " + methodName + "!");
        } else {
            return paymentMethodModels;
        }

    }

    @Override
    public PaymentMethodModel createPaymentMethod(PaymentMethodModel paymentMethodModel) {
        return paymentMethodRepository.save(paymentMethodModel);
    }

    @Override
    public PaymentMethodModel updatePaymentMethod(Long paymentMethodID, PaymentMethodModel updatedPaymentMethodModel) {
        PaymentMethodModel existingPaymentMethod = paymentMethodRepository.findById(paymentMethodID)
                .orElseThrow(() -> new NoSuchElementException("Payment Method not found with ID: " + paymentMethodID));

        // Update fields as needed
        existingPaymentMethod.setMethodName(updatedPaymentMethodModel.getMethodName());

        return paymentMethodRepository.save(existingPaymentMethod);
    }

    @Override
    public void deletePaymentMethod(Long paymentMethodID) {
        if (!paymentMethodRepository.existsById(paymentMethodID)) {
            throw new NoSuchElementException("Payment Method not found with ID: " + paymentMethodID);
        }
        paymentMethodRepository.deleteById(paymentMethodID);
    }

    @Override
    public PaymentMethodModel getPaymentMethodById(Long paymentMethodID) {
        // Query the database for the payment method by ID
        return paymentMethodRepository.findByPaymentMethodID(paymentMethodID);
    }
}
