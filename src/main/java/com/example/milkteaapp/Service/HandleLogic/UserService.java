package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.UserModel;
import com.example.milkteaapp.Repository.UserRepository;
import com.example.milkteaapp.Service.Interface.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private  final UserRepository userResponsitory;

    private final CartDetailService cartDetailRepository;
    private final  OrderService orderRepository;

    public UserService(UserRepository userResponsitory,CartDetailService cartDetailRepository,
                       OrderService orderRepository) {
        this.userResponsitory = userResponsitory;
        this.cartDetailRepository = cartDetailRepository;
        this.orderRepository = orderRepository;
    }
    //Hàm lấy tất cả danh sách khách hàng
    @Override
    public List<UserModel> getAllUser() {
        return userResponsitory.findAll();
    }
//Hàm thêm khách hàng
public String generateUserID() {
    // Truy vấn mã User lớn nhất hiện tại
    String lastUserID = userResponsitory.findMaxUserID(); // VD: US003

    // Nếu không có User nào trong cơ sở dữ liệu
    if (lastUserID == null) {
        return "US001";
    }

    // Lấy phần số từ mã UserID (VD: 003 từ US003)
    int numberPart = Integer.parseInt(lastUserID.substring(2));

    // Tăng phần số lên 1 (VD: 003 -> 004)
    int newNumber = numberPart + 1;

    // Tạo mã UserID mới với định dạng "US" + 3 chữ số
    return String.format("US%03d", newNumber);  // VD: US004
}
        @Override
    public UserModel addUser(UserModel userModel) {
        Optional<UserModel> customermodel1= userResponsitory.findById(userModel.getUserID());
        if(customermodel1.isEmpty()){
            return userResponsitory.save(userModel);
        }
        return null;
    }
//Hàm tìm kiếm khách hàng
    @Override
    public List<UserModel>  searchUserByName(String displayName) {
        List<UserModel> customermodel1= userResponsitory.findByDisplayName(displayName);
        if(customermodel1.isEmpty()){
            throw new EntityNotFoundException("Không tìm thấy "+displayName);
        }
        return customermodel1;
    }

    @Override
    public UserModel updateUser(String userID, UserModel userModel) {
        // Tìm kiếm khách hàng hiện tại trong cơ sở dữ liệu dựa trên ID
        Optional<UserModel> existingCustomerOpt = userResponsitory.findById(userID);

        if (existingCustomerOpt.isPresent()) {
            // Lấy khách hàng hiện tại từ Optional
            UserModel existingCustomer = existingCustomerOpt.get();

            // Cập nhật các thuộc tính cần thiết từ customerModel
            updateCustomerDetails(existingCustomer, userModel);

            // Lưu khách hàng đã cập nhật và trả về kết quả
            return userResponsitory.save(existingCustomer);
        } else {
            // Trả về null hoặc có thể ném ngoại lệ tùy thuộc vào yêu cầu của bạn
            throw new EntityNotFoundException("Customer with ID " + userID + " not found.");
        }
    }

    /**
     * Phương thức hỗ trợ để cập nhật thông tin khách hàng từ model mới.
     */
    private void updateCustomerDetails(UserModel existingCustomer, UserModel newCustomerDetails) {
        existingCustomer.setDisplayName(newCustomerDetails.getDisplayName());
        existingCustomer.setPhone(newCustomerDetails.getPhone());
        existingCustomer.setEmail(newCustomerDetails.getEmail());
        existingCustomer.setAddress(newCustomerDetails.getAddress());

    }


    // Hàm xóa khách hàng
    @Override
    public boolean deleteUser(String userID) {
        try {
            Optional<UserModel> existingUser = userResponsitory.findById(userID);

            if (existingUser.isPresent()) {

                UserModel user = existingUser.get();

                if (user.getAccount() == null) {
                    userResponsitory.deleteById(userID);
                    return true;
                } else {
                    userResponsitory.deleteById(userID);
                    return true;
                }
            } else {
                return false; // Không tìm thấy người dùng
            }
        } catch (Exception e) {
            System.err.println("Error while deleting user: " + e.getMessage());
            e.printStackTrace();  // Log thêm thông tin chi tiết về lỗi
            throw new RuntimeException("Failed to delete user with ID: " + userID, e);
        }
    }


    public List<UserModel> findByName(String displayName) {
        return userResponsitory.findByDisplayNameContainingIgnoreCase(displayName);
    }

    @Override
    public String getUserDisplayName(String userID) {
        // Tìm kiếm người dùng theo userID bằng phương thức findByUserID từ repository
        Optional<UserModel> optionalUser = userResponsitory.findByUserID(userID);

        // Kiểm tra nếu người dùng có tồn tại
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            return user.getDisplayName();  // Trả về displayName của người dùng
        }

        // Trả về null nếu không tìm thấy người dùng
        return null;
    }



//    @Autowired
//    private CustomerResponsitory customerRepository;
//
//    @Override
//    public List<CustomersModel> getAllCustomers() {
//        return customerRepository.findAll();
//    }
//
//    @Override
//    public CustomersModel addCustomer(CustomersModel customersModel) {
//        if (customersModel != null) {
//            return customerRepository.save(customersModel);
//        }
//        return null;
//    }
//
//    @Override
//    public CustomersModel searchCustomerByID(long customerId) {
//        return customerRepository.findById(customerId).orElse(null);
//    }
//
//    @Override
//    public CustomersModel updateCustomer(long customerId, CustomersModel customersModel) {
//        Optional<CustomersModel> existingCustomer = customerRepository.findById(customerId);
//        if (existingCustomer.isPresent()) {
//            CustomersModel customerToUpdate = existingCustomer.get();
//            customerToUpdate.setFirstName(customersModel.getFirstName());
//            customerToUpdate.setLastName(customersModel.getLastName());
//            customerToUpdate.setAddress(customersModel.getAddress());
//            customerToUpdate.setCity(customersModel.getCity());
//            customerToUpdate.setCountry(customersModel.getCountry());
//            customerToUpdate.setPhone(customersModel.getPhone());
//            customerToUpdate.setEmail(customersModel.getEmail());
//            return customerRepository.save(customerToUpdate);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean deleteCustomer(long customerId) {
//        if (customerRepository.existsById(customerId)) {
//            customerRepository.deleteById(customerId);
//            return true;
//        }
//        return false;
//    }
}
//package com.example.milkteaapp.Service.HandleLogic;
//
//import com.example.milkteaapp.Model.CustomerModel;
//import com.example.milkteaapp.Repository.CustomerResponsitory;
//import com.example.milkteaapp.Service.Interface.ICustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CustomerService implements ICustomerService {
//
//    @Autowired
//    private CustomerResponsitory customerResponsitory;
//
//    @Override
//    public List<CustomerModel> getAllCustomers() {
//        return customerResponsitory.findAll();
//    }
//
//    @Override
//    public CustomerModel addCustomer(CustomerModel customerModel) {
//        if (customerModel == null || customerModel.getCustomerID() == null) {
//            throw new IllegalArgumentException("Invalid customer data");
//        }
//        Optional<CustomerModel> existingCustomer = customerResponsitory.findById(customerModel.getCustomerID());
//        if (existingCustomer.isEmpty()) {
//            return customerResponsitory.save(customerModel);
//        }
//        return null; // Or throw an exception if you want to signal duplicate entries
//    }
//
//    @Override
//    public CustomerModel searchCustomerByID(String customerID) {
//        return customerResponsitory.findById(customerID).orElse(null);
//    }
//
//    @Override
//    public CustomerModel updateCustomer(String customerID, CustomerModel customerModel) {
//        Optional<CustomerModel> existingCustomer = customerResponsitory.findById(customerID);
//        if (existingCustomer.isPresent()) {
//            CustomerModel customerToUpdate = existingCustomer.get();
//            customerToUpdate.setCustomerName(customerModel.getCustomerName());
//            customerToUpdate.setPhone(customerModel.getPhone());
//            customerToUpdate.setEmail(customerModel.getEmail());
//            customerToUpdate.setAddress(customerModel.getAddress());
//            customerToUpdate.setUsername(customerModel.getUsername());
//            customerToUpdate.setPasswordHash(customerModel.getPasswordHash());
//            return customerResponsitory.save(customerToUpdate);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean deleteCustomer(String customerID) {
//        if (customerResponsitory.existsById(customerID)) {
//            customerResponsitory.deleteById(customerID);
//            return true;
//        }
//        return false;
//    }
//}
