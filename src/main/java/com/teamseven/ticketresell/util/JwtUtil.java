package com.teamseven.ticketresell.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private String secretKey = "trideptrai"; //
    private int jwtExpirationInMs = 604800000; // 1 tuần

    public String generateToken(String username, String email, String fullname, String userImage, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("fullname", fullname);
        claims.put("user_image", userImage);
        claims.put("role", role);
        return createToken(claims, username);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null; // Trả về null nếu không có token hợp lệ
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Các phương thức mới để trích xuất thông tin
    public String extractEmail(String token) {
        return (String) extractAllClaims(token).get("email");
    }

    public String extractFullname(String token) {
        return (String) extractAllClaims(token).get("fullname");
    }

    public String extractUserImage(String token) {
        return (String) extractAllClaims(token).get("user_image");
    }

    public String extractUserRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }
}
