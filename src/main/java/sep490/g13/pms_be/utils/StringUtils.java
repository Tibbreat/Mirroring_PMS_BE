package sep490.g13.pms_be.utils;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.regex.Pattern;

@Service
public class StringUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXY123456789";
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String removeDiacritics(String input) {
        input = input.replaceAll("đ", "d").replaceAll("Đ", "D");
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public static String generateUsername(String fullname) {
        String[] parts = fullname.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Fullname must have at least two parts.");
        }
        String lastName = removeDiacritics(parts[parts.length - 1]);

        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            initials.append(removeDiacritics(String.valueOf(parts[i].charAt(0))));
        }
        return lastName + initials.toString();
    }

    public static String randomNumber(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(NUMBERS.length());
            result.append(NUMBERS.charAt(index));
        }
        return result.toString();
    }

    public static String randomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }
}
