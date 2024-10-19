package com.example.milkteaapp.DTO;

import lombok.Data;

@Data
public class AccountDTO {
    private String userName;
    private String passWord;
    private int role;
    private String displayName;
    private String phone;
    private String email;
    private String address;
}
