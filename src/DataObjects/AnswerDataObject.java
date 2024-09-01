package DataObjects;

/**
 * @author AJ
 */
public class AnswerDataObject extends DataObject {

    public static final String TABLE = "answers";
    private static final String[] KEYS = new String[]{
            "answer", "user_id", "question_id"
    };

    public AnswerDataObject() {
        super(TABLE);
    }

    public AnswerDataObject(String answer, String userID, String questionID) {
        super(TABLE, KEYS, answer, userID, questionID);
    }

    public static String[] KEYS() {
        return KEYS;
    }

    public static String answerKey() {
        return KEYS[0];
    }

    public static String userIDKey() {
        return KEYS[1];
    }

    public static String questionIDKey() {
        return KEYS[2];
    }

    @Override
    public String[] keys() {
        return KEYS;
    }

}
