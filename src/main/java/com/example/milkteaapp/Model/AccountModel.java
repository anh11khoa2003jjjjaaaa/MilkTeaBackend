package com.example.milkteaapp.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accounts")
public class AccountModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "AccountID", updatable = false, nullable = false)
    private String accountID;

    @Column(name = "Username", nullable = false, length = 255, unique = true)
    private String userName;

    @Column(name = "Password", nullable = false, length = 255)
    private String passWord;

    @Column(name = "Role", nullable = false)
    private Integer role; // 1 for user, 2 for admin

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserModel user;

}
