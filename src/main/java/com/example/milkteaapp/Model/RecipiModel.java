package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Recipis")
public class RecipiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecipeID")
    private Long recipeID;

    @Column(name = "ProductID", nullable = false, length = 255)
    private String productID;

    @Column(name = "IngredientID", nullable = false, length = 255)
    private Long ingredientID;

    @Column(name = "Quantity", nullable = false, precision = 18, scale = 2)
    private BigDecimal quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ProductID", insertable = false, updatable = false)
    private ProductModel product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "IngredientID", insertable = false, updatable = false)
    private IngredientModel ingredient;
}