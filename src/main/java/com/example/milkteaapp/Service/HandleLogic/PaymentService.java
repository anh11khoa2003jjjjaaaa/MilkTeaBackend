//package com.example.milkteaapp.Service.HandleLogic;
//
//import com.example.milkteaapp.Config.VnPayConfig;
//import com.example.milkteaapp.Model.PaymentModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//@Service
//public class PaymentService {
//
//    @Autowired
//    private VnPayConfig vnPayConfig;
//
//    public String createVnpayPaymentUrl(PaymentModel paymentModel, long amount, String ipAddress) throws Exception {
//        // VNPAY config data
//        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();
//        String vnp_HashSecret = vnPayConfig.getVnp_HashSecret();
//        String vnp_Url = vnPayConfig.getVnp_Url();
//        String vnp_ReturnUrl = vnPayConfig.getVnp_ReturnUrl();
//
//        // Prepare parameters
//        String txnRef = generateTxnRef(); // Unique transaction reference
//        String orderInfo = "Thanh toán đơn hàng " + paymentModel.getOrderID();
//        String locale = "vn";
//        String currCode = "VND";
//
//        // Format date for createDate
//        String createDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//
//        // Initialize parameters map
//        SortedMap<String, String> vnp_Params = new TreeMap<>();
//        vnp_Params.put("vnp_Version", "2.1.0");
//        vnp_Params.put("vnp_Command", "pay");
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", currCode);
//        vnp_Params.put("vnp_TxnRef", txnRef);
//        vnp_Params.put("vnp_OrderInfo", orderInfo);
//        vnp_Params.put("vnp_OrderType", "billpayment");
//        vnp_Params.put("vnp_Locale", locale);
//        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", ipAddress);
//        vnp_Params.put("vnp_CreateDate", createDate);
//
//        // Generate secure hash
//        String queryString = buildQueryString(vnp_Params);
//        String secureHash = hmacSHA512(vnp_HashSecret, queryString);
//
//        // Append secure hash to query string
//        queryString += "&vnp_SecureHash=" + secureHash;
//
//        // Generate final payment URL
//        return vnp_Url + "?" + queryString;
//    }
//
//    // Generate unique transaction reference (txnRef)
//    private String generateTxnRef() {
//        // Using current time in milliseconds as txnRef or you can use UUID
//        return String.valueOf(System.currentTimeMillis());
//    }
//
//    // Method to generate query string from parameters
//    private String buildQueryString(SortedMap<String, String> params) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            if (sb.length() > 0) sb.append("&");
//            sb.append(entry.getKey()).append("=").append(entry.getValue());
//        }
//        return sb.toString();
//    }
//
//    // Method to generate HMAC SHA512 hash
//    private String hmacSHA512(String secretKey, String data) throws NoSuchAlgorithmException, InvalidKeyException {
//        Mac mac = Mac.getInstance("HmacSHA512");
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
//        mac.init(secretKeySpec);
//        byte[] hash = mac.doFinal(data.getBytes());
//        return bytesToHex(hash).toUpperCase();
//    }
//
//    // Convert bytes to Hex string
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//}
package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.PaymentModel;
import com.example.milkteaapp.Repository.PaymentRepository;
import com.example.milkteaapp.Service.Interface.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {
@Autowired
private PaymentRepository paymentRepository;

    @Override
    public List<PaymentModel> getAllPayments() {
        return List.of();
    }

    @Override
    public PaymentModel getPaymentById(Long paymentID) {
        return null;
    }
//Thêm thanh toán
    public PaymentModel createPayment(PaymentModel payment) {
    return paymentRepository.save(payment);
}

    @Override
    public PaymentModel updatePayment(Long paymentID, PaymentModel updatedPaymentModel) {
        return null;
    }

    @Override
    public boolean deletePayment(Long paymentID) {
        return false;
    }

}