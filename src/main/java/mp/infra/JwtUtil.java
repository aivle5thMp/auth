package mp.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import mp.domain.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 24시간 (밀리초)
    
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId());
        claims.put("role", user.getRole());
        claims.put("is_subscribed", user.getIsSubscribed());
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public Long extractUserId(String token) {
        return extractAllClaims(token).get("sub", Long.class);
    }
    
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    
    public Boolean extractIsSubscribed(String token) {
        return extractAllClaims(token).get("is_subscribed", Boolean.class);
    }
    
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
} 