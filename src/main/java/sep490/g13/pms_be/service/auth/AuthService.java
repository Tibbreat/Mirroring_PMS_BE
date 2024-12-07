package sep490.g13.pms_be.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import sep490.g13.pms_be.model.response.auth.UserDataResponse;

public interface AuthService {
    String login(String username, String password);

    String logout(HttpServletRequest request);

    UserDataResponse getAccount(HttpServletRequest request);

    String generateCode(String email);

    String changePassword(String email, String newPassword, String reNewPassword);
}
