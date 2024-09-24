package BYteBOardDatabase;

import java.util.Arrays;
import java.util.Map;

public abstract class DBOperation {

    protected static final String TABLE_KEY_DELIMITER = ":";
    protected static final String KEY_VALUE_DELIMITER = "=";
    protected static final String KEYS_DELIMITER = ",";
    public static final String AND = " AND ";
    public static final String COUNT = "COUNT";
    public static final String MAX = "MAX";
    public static final String DESC = "DESC";
    public static final String ASC = "ASC";
    protected String[] keys;
    private final String table;

    protected final Map<String, String> keyValueMap;

    public DBOperation(Map<String, String> keyValueMap, String table, String... keys) {
        this.keyValueMap = keyValueMap;
        this.table = table;
        this.keys = keys;
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
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValuesBy(
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @return a DB result by taking multiple keys with condition
     * @see #findValues(String...)
     * @see #findValuesBy(boolean, String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValuesBy(String condition, String... selectKeys) {
        return findValuesBy(false, condition, selectKeys);
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValuesBy(true,
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @return a DB result by taking multiple keys with condition to find distinct value
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
     * @param condition where condition for query
     * @param aggregateFunction the column Key used in clauses: COUNT('Key') | GROUP BY 'Key'
     * @param additionalOrderByKey the column Key that is to order values in ascending if the count is same
     * @param selectKeysFromTable the tableKeys to select
     * @return the values in ascending count(of specified column)
     * @see #joinValuesBy(boolean, String, String[], String...)
     * */
    public DBDataObject[] findTopValuesBy(String condition, String aggregateFunction, String additionalOrderByKey, String[] onConditions, int limit, String... selectKeysFromTable) {
        return DatabaseManager.getTop(selectKeysFromTable, onConditions, condition, aggregateFunction, additionalOrderByKey, String.valueOf(limit));
    }

    /**
     * @return the values in ascending count(of specified column)
     * @see #findTopValuesBy(String, String, String, String[], int, String...)
     * */
    public DBDataObject[] findTopValues(String aggregateFunction, String additionalOrderByKey, String[] onConditions, int limit, String... selectKeysFromTable) {
        return findTopValuesBy(null, aggregateFunction, additionalOrderByKey, onConditions, limit, selectKeysFromTable);
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValues(DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @return a DB result by taking multiple keys
     * @see #findValues(boolean, String...)
     * @see #findValuesBy(String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValues(String... selectKeys) {
        return findValuesBy(null, selectKeys);
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBUser.ops.findValues(true, DBUser.ID, DBUser.NAME, ...);
     * }</pre></blockquote>
     * @return a DB DISTINCT result by taking multiple keys
     * @see #findValues(String...)
     * @see #findValuesBy(String, String...)
     * @see #matchByKey(String, String)
     * @see #matchByValue(String, String)
     * */
    public DBDataObject[] findValues(boolean isDistinct, String... selectKeys) {
        return findValuesBy(isDistinct, null, selectKeys);
    }

    /**
     * The size of onConditions passed should be one less than size of selectKeysFromTables.<br>
     * Keep clauses null if not needed.
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValuesBy(
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  new String[] { DBUser.ops.matchByKey(DBUser.ID, DBStudent.ROLL_NO) },
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     * @return a DB result by Joining Values by clauses - WHERE and ON(after JOIN)
     * @see #joinValues(String...)
     * @see #joinValuesBy(boolean, String, String[], String...)
     * @see #matchByValue(String, String)
     * @see #matchByKey(String, String)
     */
    public DBDataObject[] joinValuesBy(String whereCondition, String[] onConditions, String... selectKeysFromTables) {
        return joinValuesBy(false, whereCondition, onConditions, selectKeysFromTables);
    }

    /**
     * The size of onConditions passed should be one less than size of selectKeysFromTables.<br>
     * Keep clauses null if not needed.
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValuesBy(true,
     *                  DBUser.ops.matchByValue(DBUser.NAME, "name_of_user"),
     *                  new String[] { DBUser.ops.matchByKey(DBUser.ID, DBStudent.ROLL_NO) },
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     * @return a DB DISTINCT result by Joining Values by clauses - WHERE and ON(after JOIN)
     * @see #joinValues(String...)
     * @see #joinValuesBy(String, String[], String...)
     * @see #matchByValue(String, String)
     * @see #matchByKey(String, String)
     */
    public DBDataObject[] joinValuesBy(boolean isDistinct, String whereCondition, String[] onConditions, String... selectKeysFromTables) {
        DBDataObject[] dataSet = DatabaseManager.get(selectKeysFromTables, onConditions, whereCondition, isDistinct);

        if(dataSet == null)
            throw new RuntimeException("Cannot fetch DB to find values by input: " + Arrays.toString(selectKeysFromTables));

        return dataSet;
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValues(
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     * @return a DB result by taking multiple tables (["table1:key1,key2", "table2:key1,key2", ...])
     * @see #appendKeys(String...)
     */
    public DBDataObject[] joinValues(String... selectKeysFromTables) {
        return joinValues(false, selectKeysFromTables);
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBDataObject[] dbResult = DBOperation.ops.joinValues(true,
     *                  DBUser.appendKeys(DBUser.ID, DBUser.NAME),
     *                  DBStudent.appendKeys(DBForm.ROLL_NO, DBUser.EMAIL_ID), ...);
     * }</pre></blockquote>
     * @return a DB DISTINCT result by taking multiple tables (["table1:key1,key2", "table2:key1,key2", ...])
     * @see #appendKeys(String...)
     * @see #joinValues(String...)
     */
    public DBDataObject[] joinValues(boolean isDistinct, String... selectKeysFromTables) {
        DBDataObject[] dataSet = DatabaseManager.get(selectKeysFromTables, null, null, isDistinct);

        if(dataSet == null)
            throw new RuntimeException("Cannot fetch DB to find values by input: " + Arrays.toString(selectKeysFromTables));

        return dataSet;
    }

    public DBDataObject[] ftSearchValueBy(String searchText, String likeCondition, String[] ftSearchKeys, String... selectKeys) {
        return DatabaseManager.ftsLikeSearch(getTable(), selectKeys, ftSearchKeys, searchText, likeCondition);
    }

    public DBDataObject[] ftSearchValue(String searchText, String[] ftSearchKeys, String... selectKeys) {
        return DatabaseManager.ftsLikeSearch(getTable(), selectKeys, ftSearchKeys, searchText, null);
    }

    public DBDataObject[] likeRelevanceSearchValue(String[] likeConditions, String... selectKeys) {
        return DatabaseManager.likeRelevanceSearch(getTable(), selectKeys, selectKeys[0], likeConditions);
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBUser.ops.matchByKey(DBUser.ID, DBStudent.ops.appendKeys(DBStudent.ROLL_NO))
     * }</pre></blockquote>
     * @return a condition statement for matching KEYS (table1.key2 = table3.key4)
     * @see #matchByValue(String, String)
     * @see #appendKeys(String...)
     * */
    public String matchByKey(String a, String b) {
        String[] table2 = b.split(":");
        return getTable() + "." + a + " = " +  table2[0] + "." + table2[1];
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      DBUser.ops.matchByValue(DBUser.ID, "123")
     * }</pre></blockquote>
     * @return a condition statement for matching KEYS (table1.key2 = table3.key4)
     * @see #matchByKey(String, String)
     * */
    public String matchByValue(String a, String b) {
        return getTable() + "." + a + " = " + DatabaseManager.PARAMETER_VALUE + b + DatabaseManager.PARAMETER_VALUE;
    }

    public String likeMatchContains(String a, String b) {
        return getLikeMatch(a, b, "%", "%");
    }

    public String likeMatchStartsWith(String a, String b) {
        return getLikeMatch(a, b, "", "%");
    }

    public String likeMatchEndsWith(String a, String b) {
        return getLikeMatch(a, b, "%", "");
    }

    private String getLikeMatch(String a, String b, String prefix, String suffix) {
        if(b == null) return null;
        return getTable() + "." + a + " LIKE " + DatabaseManager.PARAMETER_VALUE + prefix + b + suffix +  DatabaseManager.PARAMETER_VALUE;
    }

    /**
     * @param keyValues keys and values in alternate to form an even length array
     * @return an array where each element is combined key=value string
     * */
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
     * @param function COUNT, MAX, etc
     * @return  the generated result key by Database Manager for the<br>
     * aggregated function -> table_function
     * */
    public String getAggregateKey(String function) {
        return getTable() + "_" + function;
    }

    /**
     * @param function COUNT, MAX, etc
     * @param key Table column Key
     * @return aggregated statement including whitespace -> "function (table.key)"
     * */
    public String getAggregate(String function, String key) {
        return function + " (" + getTable() + "." + key + ")";
    }

    public static String addOrderBy(String key, String order) {
        return ", " + key + " " + order;
    }

    public String orderBy(String key, String order) {
        return " ORDER BY " + getTable() + "." + key + " " + order;
    }

    public static String getLastCharAndWord(String text) {
        // trim and check single-word text
        if(text == null || (text = text.trim()).isEmpty() || !text.contains(" "))
            return null;

        String[] words = text.split("\\s+");

        String secondLastWord = words[words.length - 2];
        String lastWord = words[words.length - 1];

        // last character (of the last-second word) + last word
        // note: unstable; gives unexpected results if secondLastWord's last char included
        return /*secondLastWord.charAt(secondLastWord.length() - 1) + */" " + lastWord;
    }

    /**
     * <blockquote> For example,
     * <pre>{@code
     *      String tableKeyCollection = DBUser.ops.appendKeys(DBUser.ID, DBUser.NAME, ...);
     *      System.out.print(tableKeyCollection)
     *
     *      OUTPUT -> user:id,name,...
     * }</pre></blockquote>
     * @return a table collection used by DatabaseManager for context while building query string.
     * @see #matchByValue(String, String)
     * @see #appendKeys(String...)
     * */
    public String appendKeys(String... keys) {
        return getTable() + TABLE_KEY_DELIMITER + String.join(KEYS_DELIMITER, keys);
    }

    /**
     * Appends no key but a white space.
     * Used when we want to JOIN table but not select column from it.
     * */
    public String appendEmptyKeys() {
        return getTable() + TABLE_KEY_DELIMITER + " ";
    }

    public String getTable() {
        return table;
    }

    public String[] getKeys() {
        return keys;
    }

    public void putKeyValue(String key, String value) {
        keyValueMap.put(key, value);
    }

    public Map<String, String> getKeyValueMap() {
        return keyValueMap;
    }

    public String getValue(String key) {
        if(keyValueMap == null) return null;
        return keyValueMap.get(key);
    }

    public void destroy() {
        if(keyValueMap == null) return;
        keyValueMap.clear();
    }
}
