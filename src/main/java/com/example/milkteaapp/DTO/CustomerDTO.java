package com.example.milkteaapp.DTO;

import lombok.Data;

@Data
public class CustomerDTO {

    private String customerID;
    private String customerName;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String passwordHash;
    // Thêm các thuộc tính khác nếu cần
}
