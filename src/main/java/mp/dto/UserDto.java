package mp.dto;

public class UserDto {
    
    // Request DTOs
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class LoginRequest {
        private String email;
        private String password;
        
        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    // Response DTOs
    public static class UserInfo {
        private Long userId;
        private String name;
        private String email;
        private String role;
        private Boolean isSubscribed;
        
        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Boolean getIsSubscribed() { return isSubscribed; }
        public void setIsSubscribed(Boolean isSubscribed) { this.isSubscribed = isSubscribed; }
    }
    
    public static class AuthInfo {
        private String accessToken;
        private String tokenType;
        private Integer expiresIn;
        
        // Getters and Setters
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public Integer getExpiresIn() { return expiresIn; }
        public void setExpiresIn(Integer expiresIn) { this.expiresIn = expiresIn; }
    }
    
    public static class LoginResponse {
        private UserInfo user;
        private AuthInfo auth;
        
        // Getters and Setters
        public UserInfo getUser() { return user; }
        public void setUser(UserInfo user) { this.user = user; }
        public AuthInfo getAuth() { return auth; }
        public void setAuth(AuthInfo auth) { this.auth = auth; }
    }
} 