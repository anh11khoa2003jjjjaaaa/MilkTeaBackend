package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.EmployeeModel;
import com.example.milkteaapp.Service.HandleLogic.EmployeeService;
import com.example.milkteaapp.Service.Interface.IEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Lấy danh sách tất cả nhân viên
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeModel>> getAllEmployees() {
        List<EmployeeModel> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addEmployee(@RequestBody EmployeeModel employeeModel) {
        try {
            EmployeeModel newEmployee = employeeService.addEmployee(employeeModel);
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception and return an error response
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật thông tin một nhân viên
    @PutMapping("/update/{employeeID}")
    public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable Long employeeID, @RequestBody EmployeeModel employeeModel) {
        EmployeeModel updatedEmployee = employeeService.updateEmployee(employeeID, employeeModel);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    // Xóa một nhân viên
    @DeleteMapping("/delete/{employeeID}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeID) {
        boolean isDeleted = employeeService.deleteEmployee(employeeID);
        if (isDeleted) {
            return new ResponseEntity<>("Nhân viên đã được xóa thành công!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Xóa nhân viên thất bại!", HttpStatus.NOT_FOUND);
        }
    }

    // Tìm kiếm nhân viên theo ID
//    @GetMapping("/{employeeID}")
//    public ResponseEntity<EmployeeModel> findEmployeeById(@PathVariable Long employeeID) {
//        EmployeeModel employee = employeeService.findEmployeeById(employeeID);
//        return new ResponseEntity<>(employee, HttpStatus.OK);
//    }

    // Tìm kiếm nhân viên theo tên
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeModel>> searchEmployeesByName(@RequestParam(required = false) String employeeName) {
        List<EmployeeModel> employees = employeeService.searchEmployeesByName(employeeName);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
