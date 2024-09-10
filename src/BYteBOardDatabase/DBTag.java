package BYteBOardDatabase;

public class DBTag extends DBOperation {

    public static final DBTag ops = new DBTag();
    public static final String TABLE = "question_tags";
    public static final String K_TAG_ID = "tag_id";
    public static final String K_TAG = "tag";
    public static final String K_QUESTION_ID = "tag_question_id";

    public DBTag() {
        super(null, TABLE, K_QUESTION_ID, K_TAG_ID, K_TAG);
    }

    public void addTag(String tag, String questionID) {
        ops.insertValue(new String[]{K_TAG, K_QUESTION_ID}, new String[]{tag, questionID});
    }
}
