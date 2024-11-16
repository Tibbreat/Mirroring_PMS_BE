package sep490.g13.pms_be.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sep490.g13.pms_be.exception.jwt.JwtTokenException;
import sep490.g13.pms_be.exception.jwt.JwtTokenExpiredException;
import sep490.g13.pms_be.exception.jwt.JwtTokenMalformedException;
import sep490.g13.pms_be.exception.jwt.JwtTokenUnsupportedException;

@ControllerAdvice
public class JwtTokenExceptionHandler {


    @ExceptionHandler(JwtTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleJwtTokenException(JwtTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleJwtTokenExpiredException(JwtTokenExpiredException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(JwtTokenMalformedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleJwtTokenMalformedException(JwtTokenMalformedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(JwtTokenUnsupportedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleJwtTokenUnsupportedException(JwtTokenUnsupportedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }
}
