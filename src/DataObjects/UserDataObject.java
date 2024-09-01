/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataObjects;

/**
 * @author AJ
 */
public class UserDataObject extends DataObject {

    public static final String TABLE = "qna_users";
    private static final String[] KEYS = new String[]{
            "username", "password", "email", "user_profile"
    };

    public UserDataObject() {
        super(TABLE);
    }

    public UserDataObject(String username, String password, String email) {
        this(username, password, email, "0");
    }

    public UserDataObject(String username, String password, String email, String userProfile) {
        super(TABLE, KEYS, username, password, email, userProfile);
    }

    public static String[] KEYS() {
        return KEYS;
    }

    public static String usernameKey() {
        return KEYS[0];
    }

    public static String passwordKey() {
        return KEYS[1];
    }

    public static String emailKey() {
        return KEYS[2];
    }

    public static String profileKey() {
        return KEYS[3];
    }

    public static String userIDKey() {
        return "user_id";
    }

    @Override
    public String[] keys() {
        return KEYS;
    }

}
