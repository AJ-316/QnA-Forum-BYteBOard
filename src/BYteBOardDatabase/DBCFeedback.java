package BYteBOardDatabase;

public class DBCFeedback extends DBOperation {

    public static final DBCFeedback ops = new DBCFeedback();
    public static final String TABLE = "qna_comments_feedback";
    public static final String K_FEEDBACK_ID = "feedback_id";
    public static final String K_USER_ID = "feedback_user_id";
    public static final String K_COMMENT_ID = "feedback_comment_id";
    public static final String K_FEEDBACK = "feedback";

    public static final String V_FEEDBACK_NONE = "0";
    public static final String V_FEEDBACK_USEFUL = "1";

    public DBCFeedback() {
        super(null, TABLE, K_FEEDBACK_ID, K_FEEDBACK, K_USER_ID, K_COMMENT_ID);
    }

    public static String getFeedback(String feedbackUserID, String commentID) {
        DBDataObject commentData = fetchCommentData(feedbackUserID, commentID);

        if(commentData == null)
            return V_FEEDBACK_NONE;

        return commentData.getValue(K_FEEDBACK);
    }

    public static void giveFeedback(String feedbackUserID, String commentID, String feedback) {
        DBDataObject commentData = fetchCommentData(feedbackUserID, commentID);

        if(commentData == null) {

            // user has never given feedback to this comment; insert new
            ops.insertValue(new String[]{K_USER_ID, K_COMMENT_ID, K_FEEDBACK},
                    new String[]{feedbackUserID, commentID, feedback});

            return;
        }

        // user has given feedback to this comment; update it
        ops.updateValueBy(ops.matchByValue(K_COMMENT_ID, commentID) +
                AND + ops.matchByValue(K_USER_ID, feedbackUserID), K_FEEDBACK, feedback);
    }

    private static DBDataObject fetchCommentData(String userID, String commentID) {
        DBDataObject[] voteData = ops.findValuesBy(
                ops.matchByValue(K_USER_ID, userID) + DBOperation.AND +
                        ops.matchByValue(K_COMMENT_ID, commentID), "*");
        if(voteData.length == 0) return null;
        return voteData[0];
    }

    public static boolean isFeedbackUseful(String feedback) {
        return feedback != null && feedback.equals(V_FEEDBACK_USEFUL);
    }
}
