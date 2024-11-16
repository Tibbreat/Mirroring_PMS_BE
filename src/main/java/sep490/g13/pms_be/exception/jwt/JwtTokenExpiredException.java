package sep490.g13.pms_be.exception.jwt;

public class JwtTokenExpiredException extends JwtTokenException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}
