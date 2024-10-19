package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Service.HandleLogic.AccountService;
import com.example.milkteaapp.Service.HandleLogic.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4300","http://localhost:4200"})
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtService jwtService;
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AccountModel accountRequest) {
//        try {
//            // Kiểm tra thông tin đăng nhập
//            AccountModel account = accountService.login(accountRequest.getUserName(), accountRequest.getPassWord());
//
//            // Nếu tài khoản không hợp lệ, trả về lỗi 401 (Unauthorized)
//            if (account == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(Map.of("error", "Invalid username or password"));
//            }
//
//            // Tạo token JWT cho người dùng sau khi đăng nhập thành công
//            String token = jwtService.generateToken(account.getUserName(),account.getRole().toString());
//
//            // Trả về token và thông tin tài khoản bao gồm username và role
//            return ResponseEntity.ok(Map.of(
//                    "token", token,
//                    "userName", account.getUserName(),
//                    "role", account.getRole()
//            ));
//        } catch (Exception e) {
//            // Ghi log lỗi và trả về thông báo lỗi 500 (Internal Server Error) kèm với chi tiết lỗi
//            System.err.println("Login error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "An error occurred during login. Please try again later."));
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountModel accountRequest) {
        try {
            // Kiểm tra thông tin đăng nhập
            AccountModel account = accountService.login(accountRequest.getUserName(), accountRequest.getPassWord());

            // Nếu tài khoản không hợp lệ
            if (account == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
            }

            // Tạo token
            String token = jwtService.generateToken(account.getUserName(),account.getUser().getUserID());

            // Trả về token và thông tin tài khoản
            return ResponseEntity.ok().body(Map.of("token", token, "userName", account.getUserName(),"role", account.getRole()));
        } catch (Exception e) {
            // Ghi log cho thông báo lỗi
            System.err.println("Login error: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

//        @PostMapping("/register")
//    public ResponseEntity<Map<String, Object>> registerAccount(@RequestBody AccountDTO request) {
//        try {
//            // Tạo AccountModel và UserModel từ AccountDTO
//            AccountModel account = new AccountModel();
//            account.setAccountID(generateAccountID());
//            account.setUserName(request.getUserName());
//            account.setPassWord(request.getPassWord());  // Mật khẩu sẽ được mã hóa trong service
//            account.setRole(request.getRole());
//
//            UserModel user = new UserModel();
//            user.setDisplayName(request.getDisplayName());
//            user.setPhone(request.getPhone());
//            user.setEmail(request.getEmail());
//            user.setAddress(request.getAddress());
//
//            // Đăng ký tài khoản và người dùng
//            AccountModel newAccount = accountService.registerAccount(account, user);
//            // Trả về phản hồi thành công
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(Map.of("success", "Đăng ký thành công!", "account", newAccount));
//        } catch (IllegalStateException e) {
//            // Xử lý lỗi nếu có
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            // Xử lý lỗi chung
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Đã xảy ra lỗi khi đăng ký tài khoản!", "message", e.getMessage()));
//        }
//    }
//
//    private String generateAccountID() {
//        // Giả sử bạn muốn tạo ID dạng "AC001", "AC002", ...
//        int randomId = (int) (Math.random() * 1000);
//        return String.format("AC%03d", randomId);  // AC001, AC002, ...
//    }
}
