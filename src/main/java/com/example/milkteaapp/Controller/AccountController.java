package com.example.milkteaapp.Controller;

import com.example.milkteaapp.DTO.AccountDTO;
import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.UserModel;
import com.example.milkteaapp.Service.HandleLogic.AccountService;
import com.example.milkteaapp.Service.HandleLogic.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/account")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/getall")
    public ResponseEntity<Object> getAllAccount() {
        try {
            List<AccountModel> accountModels = accountService.getAllAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(accountModels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{accountID}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable String accountID) {
        try {
            // Kiểm tra nếu tài khoản có tồn tại trước khi xóa

            AccountModel account = accountService.getAccountById(accountID);
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Tài khoản không tồn tại!"));
            }

            // Tiến hành xóa tài khoản
            accountService.deleteAccountById(accountID);

            // Phản hồi thành công sau khi xóa tài khoản
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Tài khoản đã được xóa thành công!"));
        } catch (Exception e) {
            // Xử lý lỗi nếu có trong quá trình xóa
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi khi xóa tài khoản!", "message", e.getMessage()));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerAccount(@RequestBody AccountDTO request) {
        try {
            // Tạo UserModel từ AccountDTO (tạo trước vì liên quan đến Account sau đó)
            UserModel user = new UserModel();
            user.setUserID(userService.generateUserID());  // Tạo ID cho người dùng
            user.setDisplayName(request.getDisplayName());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setAddress(request.getAddress());

            // Tạo AccountModel từ AccountDTO
            AccountModel account = new AccountModel();
            account.setAccountID(generateAccountID());  // Sử dụng hàm tạo AccountID
            account.setUserName(request.getUserName());
            account.setPassWord(request.getPassWord());  // Mã hóa mật khẩu trong Service
            account.setRole(request.getRole());

            // Đảm bảo liên kết hai chiều giữa AccountModel và UserModel
            user.setAccount(account);  // Gán AccountModel cho UserModel
            account.setUser(user);     // Gán UserModel cho AccountModel (sau khi user được khởi tạo đầy đủ)

            // Lưu Account và User đồng thời
            AccountModel newAccount = accountService.registerAccount(account, user);

            // Trả về dữ liệu đã đăng ký thành công
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", "Đăng ký thành công!", "account", newAccount));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi khi đăng ký tài khoản!", "message", e.getMessage()));
        }
    }

    private String generateAccountID() {
        int randomId = (int) (Math.random() * 1000);
        return String.format("AC%03d", randomId);
    }



//
//    @PostMapping("/login")
//    public ResponseEntity<Object> login(@RequestBody AccountDTO request) {
//        // Kiểm tra đầu vào
//        if (request.getUserName() == null || request.getUserName().isEmpty() ||
//                request.getPassWord() == null || request.getPassWord().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("error", "Tên đăng nhập và mật khẩu không được để trống!"));
//        }
//
//        try {
//            // Xử lý đăng nhập và xác thực tài khoản
//            AccountModel account = accountService.login(request.getUserName(), request.getPassWord());
//
//            // Trả về phản hồi thành công
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(Map.of("success", "Đăng nhập thành công!", "account", account));
//        } catch (IllegalStateException e) {
//            // Xử lý lỗi nếu có
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            // Xử lý lỗi chung
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Đã xảy ra lỗi khi đăng nhập!", "message", e.getMessage()));
//        }
//    }




}
