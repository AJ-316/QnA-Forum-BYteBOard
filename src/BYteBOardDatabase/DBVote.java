package BYteBOardDatabase;

import CustomControls.DEBUG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DBVote extends DBOperation {

    private static Map<String, Integer> SCORE_CHANGE_MAP2;
    private static final Map<String, int[]> SCORE_CHANGE_MAP;

    public static final DBVote ops = new DBVote();
    public static final String TABLE = "qna_votes";
    public static final String K_VOTE_ID = "vote_id";
    public static final String K_VOTE_TYPE = "vote_type";
    public static final String K_QUESTION_ID = "vote_question_id";
    public static final String K_ANSWER_ID = "vote_answer_id";
    public static final String K_USER_ID = "vote_user_id";
    public static final String V_VOTE_UP = "up";
    public static final String V_VOTE_DOWN = "down";
    public static final String V_VOTE_NONE = "none";

    /*static {
        SCORE_CHANGE_MAP = new HashMap<>();
        SCORE_CHANGE_MAP.put(V_VOTE_UP + "_" + V_VOTE_DOWN, -2);
        SCORE_CHANGE_MAP.put(V_VOTE_UP + "_" + V_VOTE_NONE, -1);
        SCORE_CHANGE_MAP.put(V_VOTE_DOWN + "_" + V_VOTE_UP, 2);
        SCORE_CHANGE_MAP.put(V_VOTE_DOWN + "_" + V_VOTE_NONE, 1);
        SCORE_CHANGE_MAP.put(V_VOTE_NONE + "_" + V_VOTE_DOWN, -1);
        SCORE_CHANGE_MAP.put(V_VOTE_NONE + "_" + V_VOTE_UP, 1);
    }*/

    static {
        SCORE_CHANGE_MAP = new HashMap<>();
        SCORE_CHANGE_MAP.put(V_VOTE_UP + "_" + V_VOTE_DOWN,    new int[]{-1,  1});
        SCORE_CHANGE_MAP.put(V_VOTE_UP + "_" + V_VOTE_NONE,    new int[]{-1,  0});
        SCORE_CHANGE_MAP.put(V_VOTE_DOWN + "_" + V_VOTE_UP,    new int[]{ 1, -1});
        SCORE_CHANGE_MAP.put(V_VOTE_DOWN + "_" + V_VOTE_NONE,  new int[]{ 0, -1});
        SCORE_CHANGE_MAP.put(V_VOTE_NONE + "_" + V_VOTE_DOWN,  new int[]{ 0,  1});
        SCORE_CHANGE_MAP.put(V_VOTE_NONE + "_" + V_VOTE_UP,    new int[]{ 1,  0});
    }

    public DBVote() {
        super(null, TABLE, K_QUESTION_ID, K_VOTE_ID, K_VOTE_TYPE, K_ANSWER_ID);
    }

    public String voteAnswer(String answerID, String voterID, String voteType) {
        DEBUG.printlnRed("vote answer: ");
        DBDataObject voteData = fetchAnswerVoteData(voterID, answerID);

        if(voteData == null || voteData.getValue(K_ANSWER_ID) == null) {

            // if user never voted or voted but not this answer then add it to votes as an answer vote
            ops.insertValue(new String[]{K_USER_ID, K_ANSWER_ID, K_VOTE_TYPE},
                    new String[]{voterID, answerID, voteType});

            // update the answerer's bytescore by considering the user's last vote was "none"
            updateAnswererBytescore(answerID, V_VOTE_NONE, voteType);
            DEBUG.printlnRed("created new voter: ");
        } else if (voteData.getValue(K_ANSWER_ID) != null) {

            // update the answerer's bytescore based on the last vote cast and current vote by the user
            String oldVoteType = voteData.getValue(K_VOTE_TYPE);
            updateAnswererBytescore(answerID, oldVoteType, voteType);
            DEBUG.printlnRed("updated anwerer's score: ");
            // if user has voted this answer then update it
            ops.updateValueBy(
                    ops.matchByValue(K_USER_ID, voterID) + DBOperation.AND +
                            ops.matchByValue(K_ANSWER_ID, answerID), K_VOTE_TYPE, voteType);
            DEBUG.printlnRed("updated voter: ");
        }

        DBDataObject answerData = DBAnswer.ops.findValuesBy(
                DBAnswer.ops.matchByValue(DBAnswer.K_ANSWER_ID, answerID),
                DBAnswer.K_ANSWER_BYTESCORE)[0];

        String score = answerData.getValue(DBAnswer.K_ANSWER_BYTESCORE);
        DEBUG.printlnRed("\tscore: " + score);
        return score;
    }

    public String voteQuestion(String questionID, String voterID, String voteType) {
        DEBUG.printlnRed("vote question: ");
        DBDataObject voteData = fetchQuestionVoteData(voterID, questionID);

        if(voteData == null || voteData.getValue(K_QUESTION_ID) == null) {

            // if user never voted or voted but not this question then add it to votes as a question vote
            ops.insertValue(new String[]{K_USER_ID, K_QUESTION_ID, K_VOTE_TYPE},
                    new String[]{voterID, questionID, voteType});

            // update the questioner's bytescore by considering the user's last vote was "none"
            updateQuestionerBytescore(questionID, V_VOTE_NONE, voteType);


        } else if (voteData.getValue(K_QUESTION_ID) != null) {

            // update the questioner's bytescore based on the last vote cast and current vote by the user
            String oldVoteType = voteData.getValue(K_VOTE_TYPE);
            updateQuestionerBytescore(questionID, oldVoteType, voteType);

            // if user has voted this question then update it
            ops.updateValueBy(
                    ops.matchByValue(K_USER_ID, voterID) + DBOperation.AND +
                    ops.matchByValue(K_QUESTION_ID, questionID), K_VOTE_TYPE, voteType);
        }

        DBDataObject questionData = DBQuestion.ops.findValuesBy(
                DBQuestion.ops.matchByValue(DBQuestion.K_QUESTION_ID, questionID),
                DBQuestion.K_QUESTION_BYTESCORE)[0];

        String score = questionData.getValue(DBQuestion.K_QUESTION_BYTESCORE);
        DEBUG.printlnRed("\tscore: " + score);
        return score;
    }

    private void updateQuestionerBytescore(String questionID, String oldVoteType, String newVoteType) {
        DBDataObject questionData = DBQuestion.ops.findValuesBy(
                DBQuestion.ops.matchByValue(DBQuestion.K_QUESTION_ID, questionID), DBQuestion.K_USER_ID)[0];

        DBUser.updateBytescore(questionData.getValue(DBQuestion.K_USER_ID), getByteScore(oldVoteType, newVoteType));

        int[] votes = getVotes(oldVoteType, newVoteType);
        DBQuestion.updateBytescore(questionID, votes[0], votes[1]);
    }


    private void updateAnswererBytescore(String answerID, String oldVoteType, String newVoteType) {
        DBDataObject answerData = DBAnswer.ops.findValuesBy(
                DBAnswer.ops.matchByValue(DBAnswer.K_ANSWER_ID, answerID), DBAnswer.K_USER_ID)[0];

        DBUser.updateBytescore(answerData.getValue(DBAnswer.K_USER_ID), getByteScore(oldVoteType, newVoteType));

        int[] votes = getVotes(oldVoteType, newVoteType);
        DBAnswer.updateBytescore(answerID, votes[0], votes[1]);
    }

    private int getByteScore(String oldVoteType, String newVoteType) {
        String key = oldVoteType + "_" + newVoteType;
        int[] score = SCORE_CHANGE_MAP.getOrDefault(key, new int[] {0, 0});
        int s = score[0] + score[1] * -1;
        DEBUG.printlnRed(oldVoteType + ", " + newVoteType + ": bytescore = " + s);
        return s;
    }

    private int[] getVotes(String oldVoteType, String newVoteType) {
        String key = oldVoteType + "_" + newVoteType;
        int[] s = SCORE_CHANGE_MAP.getOrDefault(key, new int[] {0, 0});
        DEBUG.printlnRed(oldVoteType + ", " + newVoteType + ": votes = " + Arrays.toString(s));
        return s;
    }

    private DBDataObject fetchQuestionVoteData(String userID, String questionID) {
        DBDataObject[] voteData = ops.findValuesBy(
                ops.matchByValue(K_USER_ID, userID) + DBOperation.AND +
                        ops.matchByValue(K_QUESTION_ID, questionID), "*");
        if(voteData.length == 0) return null;
        return voteData[0];
    }

    private DBDataObject fetchAnswerVoteData(String userID, String answerID) {
        DBDataObject[] voteData = ops.findValuesBy(
                ops.matchByValue(K_USER_ID, userID) + DBOperation.AND +
                        ops.matchByValue(K_ANSWER_ID, answerID), "*");
        if(voteData.length == 0) return null;
        return voteData[0];
    }

    public DBDataObject[] findVotesBy(String contentKey, String userID, String contentID, String... keys) {
        return ops.findValuesBy(ops.matchByValue(K_USER_ID, userID) + DBOperation.AND + ops.matchByValue(contentKey, contentID), keys);
    }
}
