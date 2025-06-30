package mp.domain;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mp.infra.JwtUtil;

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
            throw new RuntimeException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
        
        // 필수 필드 검증
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("이메일은 필수입니다.");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("비밀번호는 필수입니다.");
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new RuntimeException("이름은 필수입니다.");
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
            throw new RuntimeException("존재하지 않는 이메일입니다.");
        }
        
        User user = userOpt.get();
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        // JWT 토큰 생성
        return jwtUtil.generateToken(user);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
} 