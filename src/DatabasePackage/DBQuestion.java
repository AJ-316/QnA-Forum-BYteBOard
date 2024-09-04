package DatabasePackage;

import java.util.Map;

public class DBQuestion extends DBOperation {

    public static final DBQuestion ops = new DBQuestion();
    public static final String TABLE = "questions";
    public static final String K_QUESTION_ID = "question_id";
    public static final String K_QUESTION_HEAD = "question_head";
    public static final String K_QUESTION_BODY = "question_body";
    public static final String K_USER_ID = "question_user_id";
    public static final String K_UPVOTES = "question_upvote";
    public static final String K_DOWNVOTES = "question_downvote";
    public static final String K_QUESTION_BYTESCORE = "question_bytescore";

    public DBQuestion() {
        super(null, TABLE, K_QUESTION_ID, K_QUESTION_HEAD, K_QUESTION_BODY, K_USER_ID, K_UPVOTES, K_DOWNVOTES, K_QUESTION_BYTESCORE);
    }

    public String addQuestion(String questionHead, String questionBody, String userID) {
        String[] keys = new String[]{K_QUESTION_HEAD, K_QUESTION_BODY, K_USER_ID};
        String[] values = new String[]{questionHead, questionBody, userID};
        return ops.insertValue(keys, values);
    }

}
