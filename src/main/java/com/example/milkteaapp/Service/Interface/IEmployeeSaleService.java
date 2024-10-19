package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.EmployeeSaleModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEmployeeSaleService {

    // Thêm EmployeeSale
    EmployeeSaleModel addEmployeeSale(EmployeeSaleModel employeeSaleModel);

    // Sửa EmployeeSale
    EmployeeSaleModel updateEmployeeSale(Long saleID, EmployeeSaleModel employeeSaleModel);

    // Xóa EmployeeSale
    boolean deleteEmployeeSale(Long saleID);

    // Tìm kiếm EmployeeSale theo EmployeeID
    List<EmployeeSaleModel> findByEmployeeID(Long employeeID);

    // Tìm kiếm EmployeeSale theo OrderID
    List<EmployeeSaleModel> findByOrderID(Long orderID);
}
