/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DatabasePackage;

import org.mindrot.jbcrypt.BCrypt;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AJ
 */
public class EncryptionUtils {

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:gmail\\.com|outlook\\.com|yahoo\\.com)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isInvalidPassword(char[] passwordChar) {
        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        Pattern pattern = Pattern.compile(passwordRegex);
        String password = new String(passwordChar);
        Matcher matcher = pattern.matcher(password);
        boolean isInvalid = !matcher.matches();
        password = "overriding in memory";

        return !matcher.matches();
    }

    public static String getPasswordFeedback(char[] passwordChar) {
        Map<String, String> passwordCriteria = new LinkedHashMap<>();
        passwordCriteria.put(".{8,}", "Length must be 8-20");
        passwordCriteria.put(".*[0-9].*", "Include a Digit");
        passwordCriteria.put(".*[a-z].*", "Include a Lowercase Character");
        passwordCriteria.put(".*[A-Z].*", "Include an Uppercase Character");
        passwordCriteria.put(".*[@#$%^&+=].*", "Include a Special Character");
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

    public static String encryptPwd(char[] pwdChar) {
        String pwd = new String(pwdChar);
        pwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
        return pwd;
    }

    public static boolean checkPwd(char[] pwdChar, String encryptedPwd) {
        String pwd = new String(pwdChar);
        boolean authenticate = BCrypt.checkpw(pwd, encryptedPwd);
        pwd = "";
        return authenticate;
    }

}
