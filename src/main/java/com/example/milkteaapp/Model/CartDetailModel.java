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
@Table(name = "CartDetails")
public class CartDetailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartDetailID")
    private Long cartDetailID;

    @Column(name = "CartID", nullable = false)
    private Long cartID;

    @Column(name = "ProductID", nullable = false, length = 255)
    private String productID;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;



    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CartID", insertable = false, updatable = false)
    private CartModel cart;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", insertable = false, updatable = false)
    private ProductModel product;

    @Column(name = "Size", nullable = false, length = 50)
    private String size;

    @Column(name = "Image", length = 255)
    private String image;
}
