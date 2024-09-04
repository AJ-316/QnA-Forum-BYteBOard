package DatabasePackage;

import java.util.Arrays;
import java.util.Map;

public abstract class DBOperation {

    public static final String TABLE_KEY_DELIMITER = ":";
    public static final String KEY_VALUE_DELIMITER = "=";
    public static final String KEYS_DELIMITER = ",";
    public static final String AND = " AND ";
    protected String[] keys;
    private final String table;

    protected final Map<String, String> keyValueMap;

    public DBOperation(Map<String, String> keyValueMap, String table, String... keys) {
        this.keyValueMap = keyValueMap;
        this.table = table;
        this.keys = keys;
    }

    // ta.findValuesBy(ta.key1, ta.key3, tb.appendKeys(tb.key4), ta.createMatchCondition(ta.key1, tb.appendKeys(tb.key1)))
    // createMatchConditionByKey() = ta.key1 = ta.key1
    // createMatchConditionByValue() = ta.key1 = var_value

    public static void main(String[] args) {
        DatabaseManager.init();
        System.out.println("\u001B[33m");

        System.out.println("\u001B[0m");

        DatabaseManager.close();
    }

    /**
     * @param updateKeysByNewValues array of alternate keys and new values
     * */
    public void updateValueBy(String condition, String... updateKeysByNewValues) {
        String[] keyValuePairs = pairByKeyValue(updateKeysByNewValues);
        DatabaseManager.update(getTable(), keyValuePairs, condition);
    }

    public void updateValue(String... updateKeysByNewValues) {
        updateValueBy(null, updateKeysByNewValues);
    }

    public String insertValueBy(String condition, String[] keys, String[] values) {
        return DatabaseManager.add(getTable(), keys, values, condition);
    }

    public String insertValue(String[] keys, String[] values) {
        return DatabaseManager.add(getTable(), keys, values, null);
    }

    /**
     * Returns a DB result by taking multiple keys with condition
     *
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValuesBy(
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @see #findValues(String...)
     * @see #findValuesBy(boolean, String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValuesBy(String condition, String... selectKeys) {
        return findValuesBy(false, condition, selectKeys);
    }

    /**
     * Returns a DB result by taking multiple keys with condition to find distinct value
     *
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValuesBy(true,
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @see #findValues(String...)
     * @see #findValuesBy(String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValuesBy(boolean isDistinct, String condition, String... selectKeys) {
        String selectKeysFromTable = appendKeys(selectKeys);

        return DatabaseManager.get(new String[]{selectKeysFromTable}, null, condition, isDistinct);
    }

    /**
     * Returns a DB result by taking multiple keys
     *
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValues(DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @see #findValues(boolean, String...)
     * @see #findValuesBy(String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValues(String... selectKeys) {
        return findValuesBy(null, selectKeys);
    }

    /**
     * Returns a DB DISTINCT result by taking multiple keys
     *
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValues(true, DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @see #findValues(String...)
     * @see #findValuesBy(String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValues(boolean isDistinct, String... selectKeys) {
        return findValuesBy(isDistinct, null, selectKeys);
    }

    /**
     * Returns a DB result by Joining Values by clauses - WHERE and ON(after JOIN)<br>
     * The size of onCondition passed should be one less than size of selectKeysFromTables.<br>
     * Keep clauses null if not needed.
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValuesBy(
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  new String[] { DBUser.ops.matchByKey(DBUser.ID, DBStudent.ROLL_NO) },
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     *
     * @see #joinValues(String...)
     * @see #joinValuesBy(boolean, String, String[], String...)
     * @see #matchByValue(String, String)
     * @see #matchByKey(String, String)
     */
    public DBDataObject[] joinValuesBy(String whereCondition, String[] onCondition, String... selectKeysFromTables) {
        return joinValuesBy(false, whereCondition, onCondition, selectKeysFromTables);
    }

    /**
     * Returns a DB DISTINCT result by Joining Values by clauses - WHERE and ON(after JOIN)<br>
     * The size of onCondition passed should be one less than size of selectKeysFromTables.<br>
     * Keep clauses null if not needed.
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValuesBy(true,
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  new String[] { DBUser.ops.matchByKey(DBUser.ID, DBStudent.ROLL_NO) },
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     *
     * @see #joinValues(String...)
     * @see #joinValuesBy(String, String[], String...)
     * @see #matchByValue(String, String)
     * @see #matchByKey(String, String)
     */
    public DBDataObject[] joinValuesBy(boolean isDistinct, String whereCondition, String[] onCondition, String... selectKeysFromTables) {
        DBDataObject[] dataSet = DatabaseManager.get(selectKeysFromTables, onCondition, whereCondition, isDistinct);

        if(dataSet == null)
            throw new RuntimeException("Cannot fetch DB to find values by input: " + Arrays.toString(selectKeysFromTables));

        return dataSet;
    }

    /**
     * Returns a DB result by taking multiple tables (["table1:key1,key2", "table2:key1,key2", ...])
     *
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValues(
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     *
     * @see #appendKeys(String...)
     */
    public DBDataObject[] joinValues(String... selectKeysFromTables) {
        return joinValues(false, selectKeysFromTables);
    }

    /**
     * Returns a DB DISTINCT result by taking multiple tables (["table1:key1,key2", "table2:key1,key2", ...])
     *
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValues(true,
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     *
     * @see #appendKeys(String...)
     * @see #joinValues(String...)
     */
    public DBDataObject[] joinValues(boolean isDistinct, String... selectKeysFromTables) {
        DBDataObject[] dataSet = DatabaseManager.get(selectKeysFromTables, null, null, isDistinct);

        if(dataSet == null)
            throw new RuntimeException("Cannot fetch DB to find values by input: " + Arrays.toString(selectKeysFromTables));

        return dataSet;
    }


    /**
     * Returns a condition statement for matching KEYS (table1.key2 = table3.key4)
     * <blockquote> For example,
     * <pre>{@code
     *      DBUser.ops.matchByKey(DBUser.ID, DBStudent.ops.appendKeys(DBStudent.ROLL_NO))
     * }</pre></blockquote>
     * @see #matchByValue(String, String)
     * @see #appendKeys(String...)
     * */
    public String matchByKey(String a, String b) {
        String[] table2 = b.split(":");
        return getTable() + "." + a + " = " +  table2[0] + "." + table2[1];
    }

    /**
     * Returns a condition statement for matching KEYS (table1.key2 = table3.key4)
     * <blockquote> For example,
     * <pre>{@code
     *      DBUser.ops.matchByValue(DBUser.ID, "123")
     * }</pre></blockquote>
     * @see #matchByKey(String, String)
     * */
    public String matchByValue(String a, String b) {
        return getTable() + "." + a + " = " + DatabaseManager.PARAMETER_VALUE + b + DatabaseManager.PARAMETER_VALUE;
    }

    public String[] pairByKeyValue(String... keyValues) {
        String[] keyValuePairs = new String[keyValues.length/2];

        for (int i = 0; i < keyValues.length; i += 2) {
            keyValuePairs[i/2] = keyValues[i] +  KEY_VALUE_DELIMITER +
                    DatabaseManager.PARAMETER_VALUE + keyValues[i + 1] +
                    DatabaseManager.PARAMETER_VALUE;
        }

        return keyValuePairs;
    }

    /**
     * Returns a table collection used by DatabaseManager for context while building query string.
     * <blockquote> For example,
     * <pre>{@code
     *      String tableKeyCollection = DBUser.ops.appendKeys(DBUser.ID, DBUser.NAME, ...);
     *      System.out.print(tableKeyCollection)
     *
     *      OUTPUT -> user:id,name,...
     * }</pre></blockquote>
     * @see #matchByValue(String, String)
     * @see #appendKeys(String...)
     * */
    public String appendKeys(String... keys) {
        return getTable() + TABLE_KEY_DELIMITER + String.join(KEYS_DELIMITER, keys);
    }

    /*public String[] appendKeys(String[]... keys) {
        String[] selectKeysFromTables = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            for (String selectKey : keys[i]) {
                selectKeysFromTables[i] = appendKeys(selectKey);
            }
        }
        return selectKeysFromTables;
    }*/

    public String getTable() {
        return table;
    }

    public String[] getKeys() {
        return keys;
    }

    public void addKeyValue(String key, String value) {
        keyValueMap.put(key, value);
    }

    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    public String getValue(String key) {
        if(keyValueMap == null) return null;
        return keyValueMap.get(key);
    }
}
