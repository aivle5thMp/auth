package mp.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mp.domain.User;
import mp.domain.UserRepository;
import mp.infra.JwtUtil;
import mp.exception.UserException;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public User register(User user) {
        // 이메일 중복 체크
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserException.EmailAlreadyExistsException(user.getEmail());
        }
        
        // 필수 필드 검증
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new UserException.RequiredFieldMissingException("이메일");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new UserException.RequiredFieldMissingException("비밀번호");
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new UserException.RequiredFieldMissingException("이름");
        }
        
        // 기본값 설정
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        
        if (user.getIsSubscribed() == null) {
            user.setIsSubscribed(false);
        }
        
        // 비밀번호 해시화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 사용자 저장 (이벤트 발행됨)
        return userRepository.save(user);
    }
    
    public String login(String email, String password) {
        // 사용자 조회
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new UserException.UserNotFoundException("존재하지 않는 이메일입니다.");
        }
        
        User user = userOpt.get();
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException.InvalidPasswordException();
        }
        
        // JWT 토큰 생성
        return jwtUtil.generateToken(user);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
} 