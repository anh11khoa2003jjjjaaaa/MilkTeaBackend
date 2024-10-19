package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.EmployeeSaleModel;
import com.example.milkteaapp.Repository.EmployeeSaleRepository;
import com.example.milkteaapp.Service.Interface.IEmployeeSaleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSaleService implements IEmployeeSaleService {

    private final EmployeeSaleRepository employeeSaleRepository;

    public EmployeeSaleService(EmployeeSaleRepository employeeSaleRepository) {
        this.employeeSaleRepository = employeeSaleRepository;
    }

    @Override
    public EmployeeSaleModel addEmployeeSale(EmployeeSaleModel employeeSaleModel) {
        return employeeSaleRepository.save(employeeSaleModel);
    }

    @Override
    public EmployeeSaleModel updateEmployeeSale(Long saleID, EmployeeSaleModel employeeSaleModel) {
        Optional<EmployeeSaleModel> existingSale = employeeSaleRepository.findById(saleID);
        if (existingSale.isPresent()) {
            EmployeeSaleModel sale = existingSale.get();
            updateSaleInfo(employeeSaleModel, sale);
            return employeeSaleRepository.save(sale);
        } else {
            throw new EntityNotFoundException("Không tìm thấy sale với ID " + saleID);
        }
    }

    private void updateSaleInfo(EmployeeSaleModel source, EmployeeSaleModel target) {
        target.setEmployeeID(source.getEmployeeID());
        target.setOrderID(source.getOrderID());
        target.setSaleDate(source.getSaleDate());
    }

    @Override
    public boolean deleteEmployeeSale(Long saleID) {
        Optional<EmployeeSaleModel> sale = employeeSaleRepository.findById(saleID);
        if (sale.isPresent()) {
            employeeSaleRepository.deleteById(saleID);
            return true;
        } else {
            throw new EntityNotFoundException("Không tìm thấy sale với ID " + saleID);
        }
    }

    @Override
    public List<EmployeeSaleModel> findByEmployeeID(Long employeeID) {
        return employeeSaleRepository.findByEmployeeID(employeeID);
    }

    @Override
    public List<EmployeeSaleModel> findByOrderID(Long orderID) {
        return employeeSaleRepository.findByOrderID(orderID);
    }
}
