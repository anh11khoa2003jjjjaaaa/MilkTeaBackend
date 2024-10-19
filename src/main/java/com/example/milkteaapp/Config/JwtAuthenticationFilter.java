//package com.example.milkteaapp.Config;
//
//import com.example.milkteaapp.Service.HandleLogic.CustomUserDetailsService;
//import com.example.milkteaapp.Service.HandleLogic.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // Lấy token từ header của request
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        // Kiểm tra xem header có chứa Bearer token không
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtService.extractUsername(jwt);
//        }
//
//        // Kiểm tra nếu username không null và người dùng chưa được xác thực
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Lấy thông tin người dùng từ UserDetailsService
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//            // Kiểm tra token có hợp lệ không
//            if (jwtService.validateToken(jwt, userDetails.getUsername())) {
//                // Tạo đối tượng Authentication cho Spring Security
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                // Đặt Authentication vào SecurityContext để xác thực thành công
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        // Tiếp tục xử lý request
//        filterChain.doFilter(request, response);
//    }
//}
//
//
package com.example.milkteaapp.Config;

import com.example.milkteaapp.Service.HandleLogic.CustomUserDetailsService;
import com.example.milkteaapp.Service.HandleLogic.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Lấy token từ header của request
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Kiểm tra xem header có chứa Bearer token không
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Bỏ "Bearer " để lấy token
            username = jwtService.extractUsername(jwt); // Trích xuất username từ token
        }

        // Kiểm tra nếu username không null và người dùng chưa được xác thực
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Lấy thông tin người dùng từ UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Kiểm tra token có hợp lệ không
            if (jwtService.validateToken(jwt, userDetails.getUsername())) {
                // Tạo đối tượng Authentication cho Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt Authentication vào SecurityContext để xác thực thành công
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Tiếp tục xử lý request
        filterChain.doFilter(request, response);
    }
}
