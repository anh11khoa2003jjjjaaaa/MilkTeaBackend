package com.example.milkteaapp.Controller;

import com.example.milkteaapp.Model.EmployeeSaleModel;
import com.example.milkteaapp.Service.Interface.IEmployeeSaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/employee_sales")
public class EmployeeSaleController {

    private final IEmployeeSaleService employeeSaleService;

    public EmployeeSaleController(IEmployeeSaleService employeeSaleService) {
        this.employeeSaleService = employeeSaleService;
    }

    @GetMapping("/employee/{employeeID}")
    public ResponseEntity<List<EmployeeSaleModel>> getSalesByEmployeeID(@PathVariable Long employeeID) {
        List<EmployeeSaleModel> sales = employeeSaleService.findByEmployeeID(employeeID);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/order/{orderID}")
    public ResponseEntity<List<EmployeeSaleModel>> getSalesByOrderID(@PathVariable Long orderID) {
        List<EmployeeSaleModel> sales = employeeSaleService.findByOrderID(orderID);
        return ResponseEntity.ok(sales);
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeSaleModel> addEmployeeSale(@RequestBody EmployeeSaleModel employeeSaleModel) {
        EmployeeSaleModel newSale = employeeSaleService.addEmployeeSale(employeeSaleModel);
        return ResponseEntity.ok(newSale);
    }

    @PutMapping("/update/{saleID}")
    public ResponseEntity<EmployeeSaleModel> updateEmployeeSale(@PathVariable Long saleID, @RequestBody EmployeeSaleModel employeeSaleModel) {
        EmployeeSaleModel updatedSale = employeeSaleService.updateEmployeeSale(saleID, employeeSaleModel);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/delete/{saleID}")
    public ResponseEntity<Void> deleteEmployeeSale(@PathVariable Long saleID) {
        boolean isDeleted = employeeSaleService.deleteEmployeeSale(saleID);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
