package com.example.milkteaapp.Service.HandleLogic;

import com.example.milkteaapp.Model.AccountModel;
import com.example.milkteaapp.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // Tìm người dùng từ cơ sở dữ liệu dựa trên username
        Optional<AccountModel> accountOptional = accountRepository.findByUserName(userName);

        // Kiểm tra nếu không tìm thấy tài khoản
        if (accountOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }

        // Lấy tài khoản từ Optional
        AccountModel account = accountOptional.get();

        // Tạo danh sách các quyền (authorities) dựa trên vai trò của người dùng
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (account.getRole() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (account.getRole() == 2) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Trả về đối tượng UserDetails
        return new org.springframework.security.core.userdetails.User(
                account.getUserName(),
                account.getPassWord(),
                authorities
        );
    }
}
