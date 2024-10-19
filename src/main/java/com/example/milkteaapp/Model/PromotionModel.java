package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Promotions")
public class PromotionModel {

    @Id
    @Column(name = "PromotionID", length = 255)
    private String promotionID;

    @Column(name = "PromotionName", nullable = false, length = 255)
    private String promotionName;

    @Column(name = "DiscountPercentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "StartDate", nullable = false)
    private Date startDate;

    @Column(name = "EndDate", nullable = false)
    private Date endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "promotion")
    private Set<PromotionDetailModel> promotionDetails;
}