
package com.example.milkteaapp.Controller;


import com.example.milkteaapp.Model.UserModel;
import com.example.milkteaapp.Service.HandleLogic.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/public/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }
    @GetMapping("getusser/{userID}")
    public ResponseEntity<Map<String, String>> getUserDisplayName(@PathVariable String userID) {
        String displayName = userService.getUserDisplayName(userID);

        Map<String, String> response = new HashMap<>();
        response.put("displayName", displayName);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/AddUser")
    public ResponseEntity<Object> addCustomer(@RequestBody UserModel userModel) {
        try {
            UserModel addedCustomer = userService.addUser(userModel);
            if (addedCustomer != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(addedCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            // Log error for debugging
            System.err.println("Error in addCustomer endpoint: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/UpdateUser/{userID}")
    public ResponseEntity<Object> updateCustomer(@PathVariable String userID, @RequestBody UserModel userModel) {
        try {
            // Đảm bảo rằng customerID từ URL và từ request body là giống nhau
            if (!userID.equals(userModel.getUserID())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            // Gọi phương thức dịch vụ để cập nhật thông tin khách hàng
            UserModel updatedCustomer = userService.updateUser(userID, userModel);
            if (updatedCustomer != null) {
                return ResponseEntity.ok(updatedCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // Log lỗi để dễ dàng debug
            System.err.println("Error in updateCustomer endpoint: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/GetAll")
    public ResponseEntity<List<UserModel>> getAllCustomers() {
        try {
            List<UserModel> customers = userService.getAllUser();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            // Xử lý lỗi và trả về mã lỗi HTTP phù hợp
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/search/{displayName}")
    public ResponseEntity<Object> getCustomerById(@PathVariable String displayName) {
        List<UserModel> customer = userService.searchUserByName(displayName);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/DeleteUser/{userID}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable String userID) {
        try {
            boolean deleted = userService.deleteUser(userID);
            if (deleted) {
                // Trả về mã 204 No Content nếu xóa thành công
                return ResponseEntity.noContent().build();
            } else {
                // Trả về mã 404 Not Found nếu không tìm thấy khách hàng để xóa
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }
        } catch (Exception e) {
            // Ghi log lỗi cho việc debug
            System.err.println("Error in deleteCustomer endpoint: " + e.getMessage());
            // Trả về mã 500 Internal Server Error nếu có lỗi trong quá trình xóa
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }

    @GetMapping("/convertUserIDtodisplayName")
    public ResponseEntity<List<UserModel>> searchUser(@RequestParam String displayName) {
        List<UserModel> users = userService.findByName(displayName);
        return ResponseEntity.ok(users);
    }
}
