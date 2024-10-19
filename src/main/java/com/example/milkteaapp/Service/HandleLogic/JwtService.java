//package com.example.milkteaapp.Service.HandleLogic;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    // Secret key for encoding and decoding the token
//    private final String SECRET_KEY = "ZyeWMAZy29Ok7GeIzG90JwX5G6PP0jifXd/O/as/W3GtZSvCeDp8RCuPmXfOxmpM";
//
//    // Token expiration time (10 hours)
//    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;
//
//    // Extract the username from the JWT token
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // Extract the expiration date from the JWT token
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    // Extract a specific claim from the JWT token
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    // Extract all claims from the JWT token
//    private Claims extractAllClaims(String token) {
//        try {
//            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
//        } catch (ExpiredJwtException e) {
//            throw new RuntimeException("JWT has expired", e);
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid JWT token", e);
//        }
//    }
//
//    // Check if the token is expired
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // Generate a new JWT token with username
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, username);
//    }
//
//    // Create a token with claims and the subject (username)
//    private String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    // Refresh the token if it is not expired
//    public String refreshToken(String token) {
//        if (isTokenExpired(token)) {
//            throw new RuntimeException("Cannot refresh an expired token");
//        }
//
//        String username = extractUsername(token);
//        Claims claims = extractAllClaims(token);
//        claims.setIssuedAt(new Date(System.currentTimeMillis()));
//        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    // Validate the token by checking username and expiration
//    public Boolean validateToken(String token, String username) {
//        final String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }
//}
package com.example.milkteaapp.Service.HandleLogic;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "ZyeWMAZy29Ok7GeIzG90JwX5G6PP0jifXd/O/as/W3GtZSvCeDp8RCuPmXfOxmpM";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    // Extract the username (subject) from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the userID from the JWT token
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userID", String.class));
    }

    // Extract expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT has expired", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate a token with both username and userID
    public String generateToken(String username, String userID) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", userID);  // Include userID in the claims
        return createToken(claims, username);
    }

    // Create a token with claims and subject
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)  // Set the username as the subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate the token by checking username and expiration
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
