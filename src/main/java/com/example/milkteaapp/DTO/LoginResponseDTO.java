package com.example.milkteaapp.DTO;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String success;
    private String role;
    private AccountDTO account;
}
