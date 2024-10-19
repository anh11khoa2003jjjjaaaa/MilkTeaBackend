package com.example.milkteaapp.Repository;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Kiểu này sử dung chỉ để làm ví dụ thôi nhe
@Repository
//Kiểu này mới được sử dụng trong dự án chính
public interface UserRepository extends JpaRepository<UserModel, String> {
    List<UserModel> findByDisplayName(String displayName);
    Optional<UserModel> findByAccount(AccountModel account);
    Optional<UserModel> findByUserID(String userID);

    @Query("SELECT u.userID FROM UserModel u ORDER BY u.userID DESC LIMIT 1")
    String findMaxUserID();
    List<UserModel> findByDisplayNameContainingIgnoreCase(String displayName);



}