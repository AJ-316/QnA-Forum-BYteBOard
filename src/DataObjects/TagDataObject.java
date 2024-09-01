/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataObjects;

/**
 * @author AJ
 */
public class TagDataObject extends DataObject {

    public static final String TABLE = "question_tags";
    private static final String[] KEYS = new String[]{
            "tag", "question_id"
    };

    public TagDataObject() {
        super(TABLE);
    }

    public TagDataObject(String tag, String questionID) {
        super(TABLE, KEYS, tag, questionID);
    }

    public static String[] KEYS() {
        return KEYS;
    }

    public static String tagKey() {
        return KEYS[0];
    }

    public static String questionIDKey() {
        return KEYS[1];
    }

    public static String tagIDKey() {
        return "tag_id";
    }

    @Override
    public String[] keys() {
        return KEYS;
    }

}
