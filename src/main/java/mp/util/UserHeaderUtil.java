package mp.util;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Gateway에서 전달한 사용자 정보 헤더를 읽는 유틸리티 클래스
 */
public class UserHeaderUtil {
    
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String USER_SUBSCRIBED_HEADER = "X-User-Subscribed";
    
    /**
     * Gateway에서 전달한 사용자 ID를 추출
     */
    public static UUID getUserId(HttpServletRequest request) {
        String userIdStr = request.getHeader(USER_ID_HEADER);
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                return UUID.fromString(userIdStr);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("유효하지 않은 사용자 ID: " + userIdStr, e);
            }
        }
        return null;
    }
    
    /**
     * Gateway에서 전달한 사용자 역할을 추출
     */
    public static String getUserRole(HttpServletRequest request) {
        return request.getHeader(USER_ROLE_HEADER);
    }
    
    /**
     * Gateway에서 전달한 구독 상태를 추출
     */
    public static Boolean isUserSubscribed(HttpServletRequest request) {
        String subscribedStr = request.getHeader(USER_SUBSCRIBED_HEADER);
        if (subscribedStr != null && !subscribedStr.isEmpty()) {
            return Boolean.parseBoolean(subscribedStr);
        }
        return false;
    }
    
    /**
     * 사용자 인증 여부 확인 (헤더에 사용자 ID가 있는지 확인)
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        return getUserId(request) != null;
    }
    
    /**
     * 관리자 권한 확인
     */
    public static boolean isAdmin(HttpServletRequest request) {
        String role = getUserRole(request);
        return "ADMIN".equals(role);
    }
    
    /**
     * 작가 권한 확인
     */
    public static boolean isAuthor(HttpServletRequest request) {
        String role = getUserRole(request);
        return "AUTHOR".equals(role);
    }
    
    /**
     * 사용자 권한 확인
     */
    public static boolean isUser(HttpServletRequest request) {
        String role = getUserRole(request);
        return "USER".equals(role);
    }
    
    /**
     * 특정 사용자 권한 확인 (자신의 정보인지 또는 관리자인지)
     */
    public static boolean hasAccessToUser(HttpServletRequest request, UUID targetUserId) {
        UUID currentUserId = getUserId(request);
        return currentUserId != null && 
               (currentUserId.equals(targetUserId) || isAdmin(request));
    }
    
    /**
     * 권한 체크 (USER, AUTHOR, ADMIN 중 하나라도 해당하는지)
     */
    public static boolean hasAnyRole(HttpServletRequest request, String... roles) {
        String userRole = getUserRole(request);
        if (userRole == null) return false;
        
        for (String role : roles) {
            if (role.equals(userRole)) {
                return true;
            }
        }
        return false;
    }
} 