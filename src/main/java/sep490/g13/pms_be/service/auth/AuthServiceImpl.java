package sep490.g13.pms_be.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sep490.g13.pms_be.entities.User;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.jwt.JwtTokenProvider;
import sep490.g13.pms_be.model.response.auth.UserDataResponse;
import sep490.g13.pms_be.repository.UserRepo;
import sep490.g13.pms_be.service.utils.EmailService;
import sep490.g13.pms_be.utils.Constant;
import sep490.g13.pms_be.utils.MailingText;
import sep490.g13.pms_be.utils.StringUtils;

import java.util.concurrent.CompletableFuture;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    public String logout(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);

        if (token == null) {
            return Constant.JWT_TOKEN_INVALID;
        }
        String username = jwtTokenProvider.getUserName(token);
        if (username == null) {
            return Constant.JWT_TOKEN_INVALID;
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getName().equals(username)) {
            securityContext.setAuthentication(null);
        }
        return Constant.AUTH_LOGOUT_SUCCESS;
    }

    public UserDataResponse getAccount(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);

        String username = jwtTokenProvider.getUserName(token);

        User userData = userRepo.findByUsername(username);
        if (userData == null) {
            throw new DataNotFoundException(Constant.DATA_NOT_FOUND);
        }
        return UserDataResponse.builder()
                .username(username)
                .id(userData.getId())
                .email(userData.getEmail())
                .phone(userData.getPhone())
                .address(userData.getAddress())
                .isActive(userData.getIsActive())
                .imageLink(userData.getImageLink())
                .role(userData.getRole().name())
                .build();
    }

    public String generateCode(String email) {
        String existedAccount = userRepo.getAccountByEmail(email);

        if (existedAccount == null) {
            throw new DataNotFoundException(Constant.EMAIL_NOT_FOUND);
        } else {
            String code = StringUtils.randomNumber(6);
            CompletableFuture.runAsync(() -> {
               emailService.sendHTMLMail(email,
                       Constant.EMAIL_SUBJECT_RESET_PASSWORD_REQUEST,
                       MailingText.htmlSendingCode(code, null, existedAccount ));
            });
            return code;
        }
    }

    public String changePassword(String email, String newPassword, String reNewPassword) {
        if (!newPassword.equals(reNewPassword)) {
            return Constant.PASSWORD_AND_CONFIRM_NOT_MATCH;
        }
        User userData = userRepo.findAccountByEmail(email);
        if (userData == null) {
            throw new DataNotFoundException(Constant.DATA_NOT_FOUND);
        }
        String encodedPass = passwordEncoder.encode(newPassword);
        userData.setPassword(encodedPass);
        userRepo.save(userData);
        return Constant.PASSWORD_CHANGE_SUCCESS;
    }
}
