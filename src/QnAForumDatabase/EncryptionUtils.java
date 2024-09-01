/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QnAForumDatabase;

import org.mindrot.jbcrypt.BCrypt;

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

    public static boolean isValidPassword(char[] passwordChar) {
        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        Pattern pattern = Pattern.compile(passwordRegex);
        String password = new String(passwordChar);
        Matcher matcher = pattern.matcher(password);
        password = "";
        return matcher.matches();
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
