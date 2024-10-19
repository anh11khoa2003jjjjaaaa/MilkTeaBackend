package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.PaymentMethodModel;
import com.example.milkteaapp.Service.HandleLogic.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/public/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllPaymentMethods() {
        try {
            List<PaymentMethodModel> paymentMethods = paymentMethodService.getAllPaymentMethods();
            return ResponseEntity.status(HttpStatus.OK).body(paymentMethods);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi truy vấn dữ liệu!", "details", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getPaymentMethodById(@RequestParam String methodName) {
        try {
            List<PaymentMethodModel> paymentMethod = paymentMethodService.search(methodName);
            return ResponseEntity.status(HttpStatus.OK).body(paymentMethod);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Phương thức thanh toán không tìm thấy với ID: " + methodName));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi truy vấn phương thức thanh toán!", "details", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addPaymentMethod(@RequestBody PaymentMethodModel paymentMethodModel) {
        try {
            PaymentMethodModel newPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethodModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPaymentMethod);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi thêm phương thức thanh toán mới!", "details", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updatePaymentMethod(@RequestParam Long paymentMethodID, @RequestBody PaymentMethodModel updatedPaymentMethodModel) {
        try {
            PaymentMethodModel updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentMethodID, updatedPaymentMethodModel);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("success", "Cập nhật phương thức thanh toán thành công!", "details", updatedPaymentMethod));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Phương thức thanh toán không tìm thấy với ID: " + paymentMethodID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi cập nhật phương thức thanh toán!", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deletePaymentMethod(@RequestParam Long paymentMethodID) {
        try {
            paymentMethodService.deletePaymentMethod(paymentMethodID);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("success", "Xóa phương thức thanh toán thành công!"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Phương thức thanh toán không tìm thấy với ID: " + paymentMethodID));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Có lỗi xảy ra khi xóa phương thức thanh toán!", "details", e.getMessage()));
        }
    }
}
