package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EmployeeSales")
public class EmployeeSaleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SaleID")
    private Long saleID;

    @NotNull
    @Column(name = "EmployeeID", nullable = false)
    private Long employeeID;

    @NotNull
    @Column(name = "OrderID", nullable = false)
    private Long orderID;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SaleDate", nullable = false)
    private Date saleDate;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", insertable = false, updatable = false)
    private EmployeeModel employee;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", insertable = false, updatable = false)
    private OrderModel order;
}