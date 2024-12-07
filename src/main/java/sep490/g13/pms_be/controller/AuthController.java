package sep490.g13.pms_be.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.model.request.auth.ChangePasswordRequest;
import sep490.g13.pms_be.model.request.auth.LoginRequest;
import sep490.g13.pms_be.model.response.AuthResponse;
import sep490.g13.pms_be.model.response.auth.UserDataResponse;
import sep490.g13.pms_be.repository.UserRepo;
import sep490.g13.pms_be.service.auth.AuthService;

@RestController
@RequestMapping("/pms/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        User userData = userRepo.findByUsername(loginRequest.getUsername());
        if (userData == null) {
            throw new BadCredentialsException("Thông tin đăng nhập không chính xác");
        }
        if (Boolean.FALSE.equals(userData.getIsActive())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(AuthResponse.builder()
                            .role(userData.getRole().name())
                            .token(null)
                            .message("Tài khoản của bạn đã bị hạn chế, liên hệ quản lý để xử lý")
                            .tokenType("Bearer")
                            .build());
        } else {
            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(AuthResponse.builder()
                            .role(userData.getRole().name())
                            .token(token)
                            .message("Đăng nhập thành công")
                            .tokenType("Bearer")
                            .uid(userData.getId())
                            .username(userData.getUsername())
                            .fullName(userData.getFullName())
                            .build());
        }
    }


    @GetMapping("/account")
    public ResponseEntity<UserDataResponse> getAccount(HttpServletRequest request) {
        return ResponseEntity.ok(authService.getAccount(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return ResponseEntity.ok(authService.logout(request));
    }

    @GetMapping("/generate-code")
    public ResponseEntity<String> generateCode(@RequestParam String email) {
        return ResponseEntity.ok(authService.generateCode(email));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(authService.changePassword(
                changePasswordRequest.getEmail(),
                changePasswordRequest.getNewPassword(),
                changePasswordRequest.getReNewPassword()));
    }
}
