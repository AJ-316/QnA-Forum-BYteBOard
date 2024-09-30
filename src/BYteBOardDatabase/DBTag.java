package BYteBOardDatabase;

public class DBTag extends DBOperation {

    public static final DBTag ops = new DBTag();
    public static final String TABLE = "qna_tags";
    public static final String K_TAG_ID = "tag_id";
    public static final String K_TAG = "tag";

    public DBTag() {
        super(null, TABLE, K_TAG_ID, K_TAG);
    }

    public static void main(String[] args) {
        DatabaseManager.init();
        DatabaseManager.PRINT_QUERY = true;
        getRelevantTags("j", true);
    }

    public static boolean isTagAvailable(String tag) {
        return ops.likeRelevanceSearchValue(new String[]{ops.likeMatchContains(K_TAG, tag)}, K_TAG).length == 0;
    }

    public static DBDataObject[] getRelevantTags(String tag, boolean includeRemaining) {
        String[] likeConditions = new String[includeRemaining ? 2 : 3];
        likeConditions[0] = ops.likeMatchStartsWith(K_TAG, tag);
        likeConditions[1] = ops.likeMatchContains(K_TAG, tag);

        return ops.likeRelevanceSearchValue(likeConditions, K_TAG, K_TAG_ID);
    }

    public static DBDataObject[] getTags(String questionID) {
        return DBQueTag.getQueTags(questionID);
    }

    public static void addTag(String tag, String questionID) {
        String tagID = ops.insertValue(new String[]{K_TAG}, new String[]{tag});
        DBQueTag.addQueTag(tagID, questionID);
    }
}
