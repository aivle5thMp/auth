package mp.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
    
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // 이메일 중복 예외
    public static class EmailAlreadyExistsException extends UserException {
        public EmailAlreadyExistsException(String email) {
            super("이미 존재하는 이메일입니다: " + email);
        }
    }
    
    // 사용자 찾지 못함 예외
    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    
    // 잘못된 비밀번호 예외
    public static class InvalidPasswordException extends UserException {
        public InvalidPasswordException() {
            super("비밀번호가 일치하지 않습니다.");
        }
    }
    
    // 필수 필드 누락 예외
    public static class RequiredFieldMissingException extends UserException {
        public RequiredFieldMissingException(String fieldName) {
            super(fieldName + "은(는) 필수입니다.");
        }
    }
} 