package mp.infra;

import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import mp.domain.*;
import mp.dto.UserDto;
import mp.exception.UserException;
import mp.dto.ErrorResponse;
import mp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/auth")
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto.RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        
        userService.register(user);
        
        UserDto.RegisterResponse response = new UserDto.RegisterResponse(true, "회원가입이 성공적으로 완료되었습니다.");
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.LoginRequest loginRequest) {
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
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        // 현재 인증된 사용자의 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID currentUserId = (UUID) authentication.getPrincipal();
        
        // 요청한 ID가 현재 사용자의 ID와 같거나 ADMIN 역할을 가진 경우에만 조회 허용
        if (currentUserId.equals(id) || authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            Optional<User> user = userService.findById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                throw new UserException.UserNotFoundException("사용자를 찾을 수 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), 
                    "FORBIDDEN", 
                    "자신의 정보만 조회할 수 있습니다.",
                    "/auth/" + id));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
//>>> Clean Arch / Inbound Adaptor
