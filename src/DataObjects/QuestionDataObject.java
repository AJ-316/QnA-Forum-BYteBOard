/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataObjects;

/**
 * @author AJ
 */
public class QuestionDataObject extends DataObject {

    public static final String TABLE = "questions";

    private static final String[] KEYS = new String[]{
            "question_head", "question_body", "user_id"
    };

    public QuestionDataObject() {
        super(TABLE);
    }

    public QuestionDataObject(String questionHead, String questionBody, String userID) {
        super(TABLE, KEYS, questionHead, questionBody, userID);
    }

    public static String[] KEYS() {
        return KEYS;
    }

    public static String questionHeadKey() {
        return KEYS[0];
    }

    public static String questionBodyKey() {
        return KEYS[1];
    }

    public static String userIDKey() {
        return KEYS[2];
    }

    public static String questionIDKey() {
        return "question_id";
    }

    @Override
    public String[] keys() {
        return KEYS;
    }

}
