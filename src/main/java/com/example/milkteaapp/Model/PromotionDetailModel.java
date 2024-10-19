package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PromotionDetails")
public class PromotionDetailModel {

    @Id
    @Column(name = "PromotionDetailID")
    private String promotionDetailID;

    @Column(name = "PromotionID", nullable = false, length = 255)
    private String promotionID;

    @Column(name = "ProductID", nullable = false, length = 255)
    private String productID;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PromotionID", insertable = false, updatable = false)
    private PromotionModel promotion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", insertable = false, updatable = false)
    private ProductModel product;
}