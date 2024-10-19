package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.EmployeeModel;
import com.example.milkteaapp.Repository.EmployeeRepository;
import com.example.milkteaapp.Service.Interface.IEmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeModel> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeModel addEmployee(EmployeeModel employeeModel) {
        return employeeRepository.save(employeeModel);
    }

    @Override
    public EmployeeModel updateEmployee(Long employeeID, EmployeeModel employeeModel) {
        Optional<EmployeeModel> existingEmployee = employeeRepository.findById(employeeID);
        if (existingEmployee.isPresent()) {
            EmployeeModel employee = existingEmployee.get();
            updateEmployeeInfo(employeeModel, employee);
            return employeeRepository.save(employee);
        } else {
            throw new EntityNotFoundException("Không tìm thấy nhân viên với ID " + employeeID);
        }
    }

    private void updateEmployeeInfo(EmployeeModel source, EmployeeModel target) {
        target.setEmployeeName(source.getEmployeeName());
        target.setPositionID(source.getPositionID());
        target.setPhone(source.getPhone());
        target.setEmail(source.getEmail());
        target.setAddress(source.getAddress());
        target.setHireDate(source.getHireDate());
//        target.setUsername(source.getUsername());
//        target.setPassword(source.getPassword());
    }

    @Override
    public boolean deleteEmployee(Long employeeID) {
        Optional<EmployeeModel> employee = employeeRepository.findById(employeeID);
        if (employee.isPresent()) {
            employeeRepository.deleteById(employeeID);
            return true;
        } else {
            throw new EntityNotFoundException("Không tìm thấy nhân viên để xóa với ID " + employeeID);
        }
    }

    @Override
    public EmployeeModel findEmployeeById(Long employeeID) {
        return employeeRepository.findById(employeeID).orElseThrow(() ->
                new EntityNotFoundException("Không tìm thấy nhân viên với ID " + employeeID));
    }

    @Override
    public List<EmployeeModel> searchEmployeesByName(String employeeName) {
        return employeeRepository.findByEmployeeNameContainingIgnoreCase(employeeName);
    }
}
