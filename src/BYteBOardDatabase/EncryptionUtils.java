/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BYteBOardDatabase;

import org.mindrot.jbcrypt.BCrypt;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AJ
 */
public class EncryptionUtils {

    // TODO: add username validation to only allow letters, numbers, underscore, fullstops in between
    public static boolean isValidEmail(final String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:gmail\\.com|outlook\\.com|yahoo\\.com)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isInvalidPassword(final char[] passwordChar) {
        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        Pattern pattern = Pattern.compile(passwordRegex);
        String password = new String(passwordChar);
        Matcher matcher = pattern.matcher(password);
        password = "overriding in memory";

        return !matcher.matches();
    }

    public static String getPasswordFeedback(final char[] passwordChar) {
        Map<String, String> passwordCriteria = new LinkedHashMap<>();
        passwordCriteria.put(".{8,}", "Length must be 8-20");
        passwordCriteria.put(".*[0-9].*", "Include Digit");
        passwordCriteria.put(".*[a-z].*", "Include Lowercase Character");
        passwordCriteria.put(".*[A-Z].*", "Include Uppercase Character");
        passwordCriteria.put(".*[@#$%^&+=].*", "Include Special Character");
        passwordCriteria.put("\\S+", "No Whitespace Allowed");

        String password = new String(passwordChar);

        for (Map.Entry<String, String> entry : passwordCriteria.entrySet()) {
            String regex = entry.getKey();
            String feedback = entry.getValue();
            if (!Pattern.matches(regex, password)) {
                return feedback;
            }
        }

        password = "overriding in memory";

        return null;
    }

    public static boolean isPasswordMatching(final char[] password, final char[] rePassword) {
        if (password.length != rePassword.length)
            return false;

        for (int i = 0; i < password.length; i++) {
            if (password[i] != rePassword[i])
                return false;
        }

        return true;
    }

    public static String encryptPwd(final char[] pwdChar) {
        String pwd = new String(pwdChar);
        pwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
        return pwd;
    }

    public static boolean checkPwd(final char[] pwdChar, final String encryptedPwd) {
        String pwd = new String(pwdChar);
        boolean authenticate = BCrypt.checkpw(pwd, encryptedPwd);
        pwd = "";
        return authenticate;
    }

}
