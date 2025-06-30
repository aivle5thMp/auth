package mp.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import mp.domain.*;
import mp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/users")
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto.RegisterRequest request) {
        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            
            User savedUser = userService.register(user);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            
            // 사용자 정보 조회
            Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());
            User user = userOpt.get();
            
            // 응답 객체 생성
            UserDto.LoginResponse response = new UserDto.LoginResponse();
            
            // 사용자 정보
            UserDto.UserInfo userInfo = new UserDto.UserInfo();
            userInfo.setUserId(user.getId());
            userInfo.setName(user.getName());
            userInfo.setEmail(user.getEmail());
            userInfo.setRole(user.getRole());
            userInfo.setIsSubscribed(user.getIsSubscribed());
            response.setUser(userInfo);
            
            // 인증 정보
            UserDto.AuthInfo authInfo = new UserDto.AuthInfo();
            authInfo.setAccessToken(token);
            authInfo.setTokenType("Bearer");
            authInfo.setExpiresIn(3600); // 24시간을 초 단위로
            response.setAuth(authInfo);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("로그인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("사용자를 찾을 수 없습니다.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
//>>> Clean Arch / Inbound Adaptor
