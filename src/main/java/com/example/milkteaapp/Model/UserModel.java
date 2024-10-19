package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class UserModel {

    @Id
    @Column(name = "UserID", length = 255)
    private String userID;

    @NotNull
    @Length(max = 255)
    @Column(name = "DisplayName", nullable = false, length = 255)
    private String displayName;

    @Pattern(regexp = "^[0-9]{1,20}$", message = "Invalid phone number")
    @Length(max = 20)
    @Column(name = "Phone", length = 20)
    private String phone;

    @Email
    @Length(max = 255)
    @Column(name = "Email", length = 255)
    private String email;

    @Length(max = 255)
    @Column(name = "Address", length = 255)
    private String address;

    @Length(max = 255)
    @Column(name = "AccountID",length = 255)
    private String accountID;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountID",insertable = false, updatable = false)
    private AccountModel account;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderModel> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomerReviewModel> customerReviews;
}
