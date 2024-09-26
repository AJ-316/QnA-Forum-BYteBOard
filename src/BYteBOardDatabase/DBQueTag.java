package BYteBOardDatabase;

public class DBQueTag extends DBOperation {

    public static final DBQueTag ops = new DBQueTag();
    public static final String TABLE = "question_tags";
    public static final String K_QUE_TAG_ID = "qt_id";
    public static final String K_TAG_ID = "qt_tag_id";
    public static final String K_QUESTION_ID = "qt_question_id";

    public static final String V_TOP_TAGS = "top_tags";

    public DBQueTag() {
        super(null, TABLE, K_QUESTION_ID, K_TAG_ID, K_QUE_TAG_ID);
    }

    public static DBDataObject[] getTopTags(int limit) {
        String tagTableKey = DBTag.ops.appendKeys(DBTag.K_TAG);

        return ops.findTopValues(ops.getAggregate(COUNT, K_TAG_ID), tagTableKey,
                new String[]{ops.matchByKey(K_TAG_ID, DBTag.ops.appendKeys(DBTag.K_TAG_ID))}, limit,
                ops.appendKeys(K_TAG_ID), tagTableKey);
    }

    /**
     * @return all the questions that use the specified tagID
     */
    public static DBDataObject[] getQuestions(String tagID, String... selectQuestionKeys) {
        return ops.joinValuesBy(
                ops.matchByValue(K_TAG_ID, tagID),

                new String[]{ops.matchByKey(K_QUESTION_ID, DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID))},
                ops.appendEmptyKeys(), DBQuestion.ops.appendKeys(selectQuestionKeys));
    }

    /**
     * @return all the questions that use the specified tagID; ordered by engagement that is
     * the questions on top will have more bytes <s>and more answers</s>
     */
    public static DBDataObject[] getQuestionsOrdered(String tagID, String... selectQuestionKeys) {
        return ops.joinValuesBy(
                ops.matchByValue(K_TAG_ID, tagID) +
                        DBQuestion.ops.orderBy(DBQuestion.K_QUESTION_BYTESCORE, DESC),

                new String[]{ops.matchByKey(K_QUESTION_ID, DBQuestion.ops.appendKeys(DBQuestion.K_QUESTION_ID))},
                ops.appendEmptyKeys(), DBQuestion.ops.appendKeys(selectQuestionKeys));
    }

    public static DBDataObject[] getQueTags(String questionID) {
        return ops.joinValuesBy(ops.matchByValue(K_QUESTION_ID, questionID),
                new String[]{ops.matchByKey(K_TAG_ID, DBTag.ops.appendKeys(DBTag.K_TAG_ID))},
                ops.appendKeys("*"), DBTag.ops.appendKeys(DBTag.K_TAG));
    }

    public static void addQueTag(String tagID, String questionID) {
        ops.insertValue(new String[]{K_TAG_ID, K_QUESTION_ID}, new String[]{tagID, questionID});
    }
}
