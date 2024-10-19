package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.RecipiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipiRepository extends JpaRepository<RecipiModel,Long> {
}
