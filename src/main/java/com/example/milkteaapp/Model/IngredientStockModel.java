package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IngredientStocks")
public class IngredientStockModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StockID")
    private Long stockID;

    @NotNull
    @Column(name = "IngredientID", nullable = false, length = 255)
    private Long ingredientID;

    @NotNull
    @Column(name = "Quantity", nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "StockDate", nullable = false)
    private Date stockDate;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IngredientID", insertable = false, updatable = false)
    private IngredientModel ingredient;
}