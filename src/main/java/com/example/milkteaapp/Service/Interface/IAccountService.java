package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAccountService {
    AccountModel registerAccount(AccountModel accountModel, UserModel userModel);
    AccountModel login(String userName, String passWord);
    List<AccountModel> getAllAccounts();
    boolean deleteAccountById(String accountID);
    AccountModel getAccountById(String accountID);
}
