package com.example.milkteaapp.DTO;



import com.example.milkteaapp.Model.OrderDetailModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String userID;
    private BigDecimal totalAmount;
    private String cancellationReason;
    private Long paymentMethodID;
    private String orderStatus;
    private String orderDate;
    private String productName;
    private List<OrderDetailModel> orderDetails;
}
