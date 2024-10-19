package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.EmployeeModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEmployeeService {

    List<EmployeeModel> getAllEmployees();

    EmployeeModel addEmployee(EmployeeModel employeeModel);

    EmployeeModel updateEmployee(Long employeeID, EmployeeModel employeeModel);

    boolean deleteEmployee(Long employeeID);

    EmployeeModel findEmployeeById(Long employeeID);

    List<EmployeeModel> searchEmployeesByName(String employeeName);
}
