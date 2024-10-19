package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Model.CartDetailModel;
import com.example.milkteaapp.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends  JpaRepository<AccountModel,String> {

    Optional<AccountModel> findByUserName(String userName);

}
