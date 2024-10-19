package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Suppliers")
public class SupplierModel {

    @Id
    @Column(name = "SupplierID", length = 255)
    private String supplierID;

    @Column(name = "SupplierName", nullable = false, length = 255)
    private String supplierName;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "Email", length = 255)
    private String email;

    @Column(name = "Address", length = 255)
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private Set<IngredientModel> ingredients;
}