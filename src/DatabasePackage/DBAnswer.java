package DatabasePackage;

public class DBAnswer extends DBOperation {

    public static final DBAnswer ops = new DBAnswer();
    public static final String TABLE = "answers";
    public static final String K_ANSWER_ID = "answer_id";
    public static final String K_ANSWER = "answer";
    public static final String K_USER_ID = "answer_user_id";
    public static final String K_QUESTION_ID = "answer_question_id";
    public static final String K_UPVOTES = "answer_upvotes";
    public static final String K_DOWNVOTES = "answer_downvotes";
    public static final String K_ANSWER_BYTESCORE = "answer_bytescore";

    public DBAnswer() {
        super(null, TABLE, K_QUESTION_ID, K_ANSWER, K_ANSWER_ID, K_USER_ID, K_UPVOTES, K_DOWNVOTES, K_ANSWER_BYTESCORE);
    }

    public static void updateBytescore(String answerID, int upvote, int downvote) {
        DBDataObject answerData = ops.findValuesBy(ops.matchByValue(K_ANSWER_ID, answerID), K_UPVOTES, K_DOWNVOTES)[0];

        int newUpvotes = Integer.parseInt(answerData.getValue(K_UPVOTES)) + upvote;
        int newDownvotes = Integer.parseInt(answerData.getValue(K_DOWNVOTES)) + downvote;

        ops.updateValueBy(ops.matchByValue(K_ANSWER_ID, answerID),
                K_UPVOTES, String.valueOf(newUpvotes), K_DOWNVOTES, String.valueOf(newDownvotes));
    }

    public void addAnswer(String answer, String userID, String questionID) {
        insertValue(new String[]{K_ANSWER, K_USER_ID, K_QUESTION_ID}, new String[]{answer, userID, questionID});
    }
}
