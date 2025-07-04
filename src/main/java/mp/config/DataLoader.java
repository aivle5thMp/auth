package mp.config;

import mp.domain.User;
import mp.domain.UserRepository;
import mp.domain.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        // 관리자 계정이 이미 존재하는지 확인
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            // 관리자 계정 생성
            User admin = new User();
            admin.setName("Administrator");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin1234")); // 비밀번호 암호화
            admin.setRole(UserRole.ADMIN);
            admin.setIsSubscribed(true);
            
            userRepository.save(admin);
            
            System.out.println("✓ 관리자 계정이 생성되었습니다.");
            System.out.println("  - 이메일: admin@admin.com");
            System.out.println("  - 비밀번호: admin1234");
            System.out.println("  - 역할: ADMIN");
        } else {
            System.out.println("ℹ 관리자 계정이 이미 존재합니다.");
        }
    }
} 