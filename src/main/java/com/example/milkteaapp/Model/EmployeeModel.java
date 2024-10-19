package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Employees")
public class EmployeeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Long employeeID;

    @NotNull
    @Column(name = "EmployeeName", nullable = false, length = 255)
    private String employeeName;

    @Column(name = "PositionID", length = 255)
    private String positionID;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    @Column(name = "Phone", length = 20)
    private String phone;

    @Email
    @Column(name = "Email", length = 255)
    private String email;

    @Column(name = "Address", length = 255)
    private String address;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HireDate", nullable = false)
    private Date hireDate;




    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PositionID", insertable = false, updatable = false)
    private PositionModel position;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EmployeeSaleModel> employeeSales;


}
