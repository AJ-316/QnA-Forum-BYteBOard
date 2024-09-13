package BYteBOardDatabase;

public class DBComment extends DBOperation {

    public static final DBComment ops = new DBComment();
    public static final String TABLE = "comments";
    public static final String K_COMMENT_ID = "comment_id";
    public static final String K_USER_ID = "comment_user_id";
    public static final String K_QUESTION_ID = "comment_question_id";
    public static final String K_ANSWER_ID = "comment_answer_id";
    public static final String K_COMMENT = "comment";
    public static final String K_COMMENT_SCORE = "comment_feedback_score";

    public DBComment() {
        super(null, TABLE, K_COMMENT_ID, K_COMMENT, K_QUESTION_ID, K_ANSWER_ID, K_USER_ID, K_COMMENT_SCORE);
    }

    public void addComment(String commenterID, String comment, String contentKey, String contentID) {
        ops.insertValue(new String[]{K_USER_ID, K_COMMENT, contentKey},
                new String[]{commenterID, comment, contentID});
    }

    public DBDataObject[] getComments(String contentKey, String contentID) {
        return ops.findValuesBy(ops.matchByValue(contentKey, contentID), "*");
    }
}
