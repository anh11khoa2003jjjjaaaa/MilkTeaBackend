//package com.example.milkteaapp.Controller;
//
//import com.example.milkteaapp.Model.PaymentModel;
//import com.example.milkteaapp.Service.HandleLogic.PaymentMethodService;
//import com.example.milkteaapp.Service.HandleLogic.PaymentService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.security.MessageDigest;
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:4300")
//@RequestMapping("/public/payment")
//public class PaymentController {
//
//
//
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @Autowired
//    private PaymentMethodService paymentMethodService;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> createPayment(@RequestBody PaymentModel paymentModel, HttpServletRequest request) {
//        try {
//            // Set payment date
//            paymentModel.setPaymentDate(LocalDateTime.now());
//
//            // Calculate total amount in "cents" (VNPAY expects amount in this format)
//            long amountInCents = paymentModel.getAmount().multiply(new BigDecimal(100)).longValue();
//
//            // Retrieve client's IP address
//            String ipAddress = request.getRemoteAddr();
//
//            // Generate the VNPAY URL
////            String paymentUrl = paymentService.createVnpayPaymentUrl(paymentModel, amountInCents, ipAddress);
//
//            // Return URL as a redirect
//            return ResponseEntity.status(303).header("Location").build();
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error occurred while processing payment request: " + e.getMessage());
//        }
//    }
//
//    private String buildQueryString(SortedMap<String, String> params) {
//        StringBuilder query = new StringBuilder();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//        }
//        query.deleteCharAt(query.length() - 1); // Remove last "&"
//        return query.toString();
//    }
//
//    private String hmacSHA512(String key, String data) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-512");
//            md.update(key.getBytes());
//            byte[] digest = md.digest(data.getBytes());
//            StringBuilder hashText = new StringBuilder();
//            for (byte b : digest) {
//                hashText.append(String.format("%02x", b));
//            }
//            return hashText.toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating secure hash", e);
//        }
//    }
//
//    @GetMapping("/vnpay_return")
//    public ResponseEntity<String> vnpayReturn(HttpServletRequest request) {
//        try {
//            Map<String, String[]> paramMap = request.getParameterMap();
//            SortedMap<String, String> vnp_Params = new TreeMap<>();
//            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
//                if (entry.getKey().startsWith("vnp_")) {
//                    vnp_Params.put(entry.getKey(), entry.getValue()[0]);
//                }
//            }
//
//            String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");
//
//            // Validate secure hash
//            String queryUrl = buildQueryString(vnp_Params);
//            String vnp_HashSecret = vnPayConfig.getVnp_HashSecret();
//            String secureHash = hmacSHA512(vnp_HashSecret, queryUrl);
//
//            if (secureHash.equals(vnp_SecureHash)) {
//                String transactionStatus = vnp_Params.get("vnp_TransactionStatus");
//                if ("00".equals(transactionStatus)) {
//                    return ResponseEntity.ok("Thanh toán thành công!");
//                } else {
//                    return ResponseEntity.ok("Thanh toán không thành công!");
//                }
//            } else {
//                return ResponseEntity.status(400).body("Chữ ký không hợp lệ!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Error processing return from VNPAY.");
//        }
//    }
//}
