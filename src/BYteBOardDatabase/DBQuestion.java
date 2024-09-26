package BYteBOardDatabase;

public class DBQuestion extends DBOperation {

    public static final DBQuestion ops = new DBQuestion();
    public static final String TABLE = "questions";
    public static final String K_QUESTION_ID = "question_id";
    public static final String K_QUESTION_HEAD = "question_head";
    public static final String K_QUESTION_BODY = "question_body";
    public static final String K_USER_ID = "question_user_id";
    public static final String K_UPVOTES = "question_upvotes";
    public static final String K_DOWNVOTES = "question_downvotes";
    public static final String K_QUESTION_BYTESCORE = "question_bytescore";

    public DBQuestion() {
        super(null, TABLE, K_QUESTION_ID, K_QUESTION_HEAD, K_QUESTION_BODY, K_USER_ID, K_UPVOTES, K_DOWNVOTES, K_QUESTION_BYTESCORE);
    }

    public static DBDataObject[] searchByQuestionHead(String question, String... selectKeys) {
        return ops.ftSearchValueBy(question,
                ops.likeMatchContains(K_QUESTION_HEAD, getLastCharAndWord(question)),
                new String[]{K_QUESTION_HEAD}, selectKeys);
    }

    public static DBDataObject[] searchByQuestion(String question, String... selectKeys) {
        return ops.ftSearchValue(question, new String[]{K_QUESTION_HEAD, K_QUESTION_BODY}, selectKeys);
    }

    public static String addQuestion(String questionHead, String questionBody, String userID) {
        String[] keys = new String[]{K_QUESTION_HEAD, K_QUESTION_BODY, K_USER_ID};
        String[] values = new String[]{questionHead, questionBody, userID};
        return ops.insertValue(keys, values);
    }

    public static void updateBytescore(String questionID, int upvote, int downvote) {
        DBDataObject questionData = ops.findValuesBy(ops.matchByValue(K_QUESTION_ID, questionID), K_UPVOTES, K_DOWNVOTES)[0];

        int newUpvotes = Integer.parseInt(questionData.getValue(K_UPVOTES)) + upvote;
        int newDownvotes = Integer.parseInt(questionData.getValue(K_DOWNVOTES)) + downvote;

        ops.updateValueBy(ops.matchByValue(K_QUESTION_ID, questionID),
                K_UPVOTES, String.valueOf(newUpvotes), K_DOWNVOTES, String.valueOf(newDownvotes));
    }

    public static DBDataObject getQuestion(String questionID) {
        DBDataObject[] questions = DBQuestion.ops.findValuesBy(
                DBQuestion.ops.matchByValue(DBQuestion.K_QUESTION_ID, questionID), "*");

        if (questions == null || questions.length == 0)
            throw new NullPointerException("Question Not Found: " + questionID);

        return questions[0];
    }

}
