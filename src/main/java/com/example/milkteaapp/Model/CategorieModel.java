package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categories")
public class CategorieModel {

    @Id
    @Column(name = "CategoryID", length = 255)
    private String categoryID;

    @Column(name = "CategoryName", nullable = false, length = 255)
    private String categoryName;

    @Column(name = "Description", length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductModel> products;
}