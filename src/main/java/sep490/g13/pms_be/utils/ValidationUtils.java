package sep490.g13.pms_be.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationUtils {

    public static String getValidationErrors(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder("Validation failed:\n");
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("\n");
        }
        return errorMessage.toString();
    }

}
