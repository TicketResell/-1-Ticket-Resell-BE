package com.teamseven.ticketresell.util;

import com.teamseven.ticketresell.config.security.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final MyUserDetailsService myUserDetailsService;
    private String secretKey = "trideptrai";
    private int jwtExpirationInMs = 604800000; // 1 tuần

    public JwtUtil(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    public String generateToken(Long id, String username, String email, String fullname, String userImage, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
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
        return null;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        try {
            // Adding a small delay to ensure different timestamps
            Thread.sleep(1); // Sleep for 1 millisecond
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }



    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String username = claims.getSubject();

        // Load user details
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null; // Nếu không tìm thấy user, trả về null
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        final String extractedUsername = extractUsername(token);
        // Giả sử bạn muốn trả về true nếu token không hết hạn
        return !isTokenExpired(token);
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

    public Long extractId(String token) {
        return ((Number) extractAllClaims(token).get("id")).longValue();
    }

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
