package com.example.milkteaapp.Config;

import com.example.milkteaapp.Service.HandleLogic.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Autowired
//    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF cho các API không sử dụng session
//                .authorizeHttpRequests(authorize -> authorize
//                        // Cho phép không cần xác thực khi truy cập các endpoint đăng nhập và đăng ký
//                        .requestMatchers("/auth/login", "/auth/register", "/IMG/**","public/product/**").permitAll()
//                        // Yêu cầu xác thực cho tất cả các yêu cầu khác
//                        .anyRequest().authenticated()
//                )
//                // Cấu hình CORS
//                .cors(cors -> cors
//                        .configurationSource(request -> {
//                            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
//                            corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200","http://localhost:4300")); // Cho phép từ localhost:4200
//                            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Các phương thức HTTP cho phép
//                            corsConfiguration.setAllowedHeaders(List.of("*")); // Cho phép tất cả các loại header
//                            corsConfiguration.setAllowCredentials(true); // Cho phép gửi thông tin xác thực như cookie, token
//                            return corsConfiguration;
//                        })
//                )
//                // Không sử dụng session trong ứng dụng (stateless)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // Thêm JWT filter để kiểm tra token trước khi UsernamePasswordAuthenticationFilter
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    @Lazy
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Cấu hình CORS
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // Cấu hình CORS cho trang admin (cổng 4200)
////        registry.addMapping("/admin/**") // Tất cả các endpoint thuộc admin
////                .allowedOrigins("http://localhost:4200") // Chỉ cho phép từ cổng 4200
////                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
////                .allowedHeaders("*")
////                .allowCredentials(true);
////
////        // Cấu hình CORS cho trang user (ví dụ: cổng 4300)
////        registry.addMapping("/user/**") // Tất cả các endpoint thuộc user
////                .allowedOrigins("http://localhost:4300") // Chỉ cho phép từ cổng 4300
////                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
////                .allowedHeaders("*")
////                .allowCredentials(true);
//        registry.addMapping("/**") // Cấu hình cho tất cả các endpoint
//                .allowedOrigins("http://localhost:4200","http://localhost:4300")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP cho phép
//                .allowedHeaders("*") // Cho phép tất cả các loại header
//                .allowCredentials(true); // Cho phép gửi cookie và thông tin xác thực
//    }
//
@Autowired
public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login",
                                "/auth/register",
                                "/IMG/**",
                                "/public/product/getAllProduct",
                                "/public/categorie/getAllCategories",
                                "/public/product/product_detail",
                                "/public/product/related",
                                "/public/product/search/**",
                                "/public/product/DisplayListProductCategory_CategoryID/**"
                                )

                        .permitAll() // Allow unauthenticated access
                        .anyRequest().authenticated() // Require authentication for other requests
                )
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:4300","http://localhost:3000"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Lazy
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:4300")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // Allow sending credentials (cookies, headers)
    }



//package com.example.milkteaapp.Config;
//
//import com.example.milkteaapp.Service.HandleLogic.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig implements WebMvcConfigurer {
//
//    private final CustomUserDetailsService userDetailsService;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Autowired
//    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF cho các API không sử dụng session
//                .authorizeHttpRequests(authorize -> authorize
//                        // Cho phép không cần xác thực khi truy cập các endpoint đăng nhập và đăng ký
//                        .requestMatchers("/auth/login", "/auth/register","/IMG/*").permitAll()
//
//                        // Cấp quyền truy cập mà không cần xác thực cho POST đến endpoint thêm sản phẩm
//                        //.requestMatchers("/public/product/addproduct").permitAll()
//                        // Yêu cầu xác thực cho tất cả các yêu cầu khác
//                        .anyRequest().authenticated()
//                )
//                // Cấu hình CORS mặc định
//                .cors(Customizer.withDefaults())
//                // Không sử dụng session trong ứng dụng (stateless)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // Thêm JWT filter để kiểm tra token trước khi UsernamePasswordAuthenticationFilter
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    @Lazy
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Cấu hình CORS
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/IMG/**")
//                .allowedOrigins("http://localhost:4200")//lưu ý
//                // Để "*" nếu bạn muốn cho phép tất cả các nguồn gốc (origin), hoặc thay đổi URL nếu chỉ định cụ thể.
//                //.allowedOrigins("*")
//                // Cho phép các phương thức HTTP
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                // Cho phép tất cả các header
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }

//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // Vô hiệu hóa CSRF nếu cần (không khuyến khích vô hiệu hóa CSRF trừ khi có lý do cụ thể)
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/public/**").permitAll()  // Cho phép truy cập vào các đường dẫn công khai
//                        .anyRequest().authenticated()  // Yêu cầu xác thực cho tất cả các yêu cầu khác
//                )
//                .formLogin(form -> form    // Kích hoạt form login mặc định của Spring Security
//                        .loginPage("/login")  // Bạn có thể định nghĩa trang login tùy chỉnh tại đây nếu muốn
//                        .permitAll()  // Cho phép tất cả người dùng truy cập trang login
//                )
//                .oauth2Login(oauth2 -> oauth2  // Kích hoạt OAuth2 login
//                        .loginPage("/oauth2/authorization/google")  // Định nghĩa trang login cho OAuth2 (sử dụng Google)
//                );
//
//        return http.build();
//    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF chỉ khi cần thiết
//                .authorizeHttpRequests(authorize -> authorize
////                        .requestMatchers("/public/**").permitAll()
//                                .requestMatchers("/auth/login").permitAll()
//                        // Cho phép truy cập vào tất cả các URL khớp với "/public/**"
//                        .anyRequest().authenticated() // Yêu cầu xác thực cho tất cả các yêu cầu khác
//                )
//                .formLogin(withDefaults());
//
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không sử dụng session
////                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm filter JWT
//
//
//        return http.build();
//    }


//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
////                .allowedOrigins("http://127.0.0.1:5500")
//                .allowedOrigins("*")// Thay đổi URL nếu cần
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*");
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://127.0.0.1:55879")// Thay đổi URL nếu cần
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);;
//
//
//    }




}

