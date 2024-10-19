package com.example.milkteaapp;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Model.UserModel;
import lombok.Data;


@Data
public class LoginRequest {
    private UserModel user;
    private AccountModel account;
}
