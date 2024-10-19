package com.example.milkteaapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Positions")
public class PositionModel {

    @Id
    @Column(name = "PositionID", length = 255)
    private String positionID;

    @Column(name = "PositionName", nullable = false, length = 255)
    private String positionName;

    @JsonIgnore
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private Set<EmployeeModel> employees;
}