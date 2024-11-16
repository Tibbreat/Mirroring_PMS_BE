package sep490.g13.pms_be.exception.jwt;

public class JwtTokenUnsupportedException extends JwtTokenException {
    public JwtTokenUnsupportedException(String message) {
        super(message);
    }
}