package com.example.milkteaapp.Service.Interface;

import com.example.milkteaapp.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IUserService {
    List<UserModel> getAllUser();
    UserModel addUser(UserModel userModel);
    List<UserModel> searchUserByName(String displayName);
    UserModel updateUser(String userID, UserModel userModel);
    boolean deleteUser(String userID);
    List<UserModel> findByName(String displayName);

    String getUserDisplayName(String userID);
}
