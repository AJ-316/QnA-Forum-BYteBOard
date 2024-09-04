package DatabasePackage;

import java.util.HashMap;
import java.util.Map;

public class DBUser extends DBOperation {

    public static final DBUser ops = new DBUser();
    public static final String TABLE = "qna_users";
    public static final String K_USER_ID = "user_id";
    public static final String K_USER_NAME = "user_name";
    public static final String K_PASSWORD = "user_password";
    public static final String K_EMAIL = "user_email";
    public static final String K_USER_PROFILE = "user_profile";
    public static final String K_USER_BYTESCORE = "user_bytescore";

    public DBUser() {
        super(null, TABLE, K_USER_ID, K_USER_NAME, K_PASSWORD, K_EMAIL, K_USER_PROFILE, K_USER_BYTESCORE);
    }

    public static void addUser(String username, String password, String email) {
        String[] keys = new String[]{K_USER_NAME, K_PASSWORD, K_EMAIL};
        String[] values = new String[]{username, password, email};
        ops.insertValue(keys, values);
    }

    public static boolean isValueAvailable(String key, String value) {
        return DatabaseManager.count(TABLE, "*", ops.matchByValue(key, value)) == 0;
    }

    public static DBDataObject accessUser(String emailOrUsername, boolean isEmail, boolean isLogged) {
        String key = isEmail ? K_EMAIL : K_USER_NAME;

        if (isValueAvailable(key, emailOrUsername))
            return null;

        if(isLogged) {
            return ops.findValuesBy(ops.matchByValue(key, emailOrUsername),
                    K_USER_ID, K_USER_NAME, K_EMAIL, K_USER_PROFILE, K_USER_BYTESCORE)[0];
        }

        return ops.findValuesBy(ops.matchByValue(key, emailOrUsername), "*")[0];
    }

    public static void updateBytescore(String userID, int score) {
        DBDataObject userData = ops.findValuesBy(ops.matchByValue(K_USER_ID, userID), K_USER_BYTESCORE)[0];

        int newBytescore = Integer.parseInt(userData.getValue(K_USER_BYTESCORE)) + score;
        ops.updateValueBy(ops.matchByValue(K_USER_ID, userID), K_USER_BYTESCORE, String.valueOf(newBytescore));
    }

    public static void updateUser(String oldUserName, String userName, String userPassword, String userEmail, String userProfileIndex) {
        Map<String, String> keyValueMap = new HashMap<>();

        if(userPassword != null)
            keyValueMap.put(K_PASSWORD, userPassword);

        keyValueMap.put(K_USER_NAME, userName);
        keyValueMap.put(K_EMAIL, userEmail);
        keyValueMap.put(K_USER_PROFILE, userProfileIndex);

        String[] keyValues = new String[keyValueMap.size()*2];

        int index = 0;

        for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
            keyValues[index++] = entry.getKey();
            keyValues[index++] = entry.getValue();
        }

        ops.updateValueBy(ops.matchByValue(K_USER_NAME, oldUserName), keyValues);
    }
}
