package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Model.UserModel;
import com.example.milkteaapp.Repository.AccountRepository;
import com.example.milkteaapp.Repository.UserRepository;
import com.example.milkteaapp.Service.Interface.IAccountService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public AccountModel addAccount(AccountModel account) {
        // Lưu AccountModel vào cơ sở dữ liệu và trả về đối tượng đã lưu
        return accountRepository.save(account);
    }




    @Override
    public AccountModel registerAccount(AccountModel accountModel, UserModel userModel) {
        // Kiểm tra xem tên người dùng đã tồn tại trong hệ thống chưa
        Optional<AccountModel> existingAccount = accountRepository.findByUserName(accountModel.getUserName());
        if (existingAccount.isPresent()) {
            throw new IllegalStateException("Tài khoản với tên người dùng '" + accountModel.getUserName() + "' đã tồn tại!");
        }

        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(accountModel.getPassWord());
        accountModel.setPassWord(encodedPassword);

        // Lưu AccountModel vào cơ sở dữ liệu
        AccountModel savedAccount = accountRepository.save(accountModel);

        // Gán AccountID và thiết lập quan hệ với UserModel
        userModel.setAccountID(savedAccount.getAccountID());
        userModel.setAccount(savedAccount);

        // Lưu UserModel vào cơ sở dữ liệu
        userRepository.save(userModel);

        // Thiết lập quan hệ hai chiều giữa UserModel và AccountModel
        savedAccount.setUser(userModel);

        // Trả về AccountModel đã lưu với UserModel không bị null
        return savedAccount;
    }


    @Override
    public AccountModel login(String userName, String passWord) {
        AccountModel account = accountRepository.findByUserName(userName)
                .orElseThrow(() -> new IllegalStateException("Tài khoản không tồn tại với tên người dùng: " + userName));

        if (!passwordEncoder.matches(passWord, account.getPassWord())) {
            throw new IllegalStateException("Mật khẩu không chính xác!");
        }
        return account;
    }

    @Override
    public List<AccountModel> getAllAccounts() {
        return accountRepository.findAll();
    }

public boolean deleteAccountById(String accountID) {
    // Xóa tài khoản từ database
    accountRepository.deleteById(accountID);
    return true;
}
    public AccountModel getAccountById(String accountID) {
        // Lấy tài khoản từ database dựa trên accountID
        return accountRepository.findById(accountID).orElse(null);
    }
}