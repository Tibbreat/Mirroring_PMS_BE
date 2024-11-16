package sep490.g13.pms_be.exception.handler;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sep490.g13.pms_be.exception.other.DataAlreadyExistException;
import sep490.g13.pms_be.exception.other.DataNotFoundException;
import sep490.g13.pms_be.exception.other.OutRangeClassException;
import sep490.g13.pms_be.model.response.base.ResponseModel;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseModel<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ResponseModel.<Map<String, String>>builder()
                .message("Dữ liệu đầu vào không hợp lệ")
                .data(errors)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleConstraintViolationExceptions(
            ConstraintViolationException ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Vi phạm ràng buộc dữ liệu")
                .data(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Loại đối số không khớp")
                .data(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Đã xảy ra lỗi")
                .data(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleDataNotFoundException(Exception ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Không tìm thấy dữ liệu")
                .data(ex.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleDataAlreadyExistException(Exception ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Dữ liệu đã tồn tại")
                .data(ex.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleBadCredentialsException(Exception ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Thông tin đăng nhập không chính xác")
                .data(ex.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OutRangeClassException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResponseModel<String>> handleOutRangeClassException(OutRangeClassException ex) {
        return new ResponseEntity<>(ResponseModel.<String>builder()
                .message("Lớp học không hợp lệ")
                .data(ex.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }
}
