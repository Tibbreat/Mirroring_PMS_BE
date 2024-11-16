package sep490.g13.pms_be.exception.jwt;

public class JwtTokenMalformedException extends JwtTokenException {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
