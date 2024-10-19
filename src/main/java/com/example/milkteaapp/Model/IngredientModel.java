package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Ingredients")
public class IngredientModel {

    @Id
    @Column(name = "IngredientID", length = 255)
    private Long ingredientID;

    @NotNull
    @Column(name = "IngredientName", nullable = false, length = 255)
    private String ingredientName;

    @Column(name = "SupplierID", length = 255)
    private String supplierID;

    @NotNull
    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplierID", insertable = false, updatable = false)
    private SupplierModel supplier;

    @JsonIgnore
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RecipiModel> recipes;

    @JsonIgnore
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<IngredientStockModel> ingredientStocks;
}