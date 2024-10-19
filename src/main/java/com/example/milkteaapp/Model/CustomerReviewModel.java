package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CustomerReviews")
public class CustomerReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private Long reviewID;

    @NotNull
    @Size(max = 255)
    @Column(name = "UserID", nullable = false, length = 255)
    private String userID;

    @NotNull
    @Size(max = 255)
    @Column(name = "ProductID", nullable = false, length = 255)
    private String productID;

    @NotNull
    @Min(1)
    @Max(5)
    @Column(name = "Rating", nullable = false)
    private Integer rating;

    @Column(name = "Comment")
    private String comment;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ReviewDate", nullable = false)
    private Date reviewDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private UserModel user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", insertable = false, updatable = false)
    private ProductModel product;
}
