package BYteBOardDatabase;

import CustomControls.DEBUG;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    public static final String PARAMETER_VALUE = "<?P_V?>";
    public static boolean PRINT_QUERY = false;
    private static Connection connection;

    public static void init() {
        establishConnection("byteboarddb_qnaforum");
    }

    public static void establishConnection(String database) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", "rootpwd");
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /*
        WITH fts_result AS (
            SELECT {col1...},
                    MATCH({col2...}) AGAINST(?{txt}) AS relevance
            FROM {table}
            WHERE MATCH({col2...}) AGAINST(?{txt})
        )

        -- First try to get results with LIKE, if no result fallback to full FTS result
        (
            SELECT {col1...},
                relevance
            FROM fts_result

            if(end != null)
                WHERE {col2...} LIKE {end}
        )
        UNION
        (
            SELECT {col1...},
                relevance
            FROM fts_result

            if(end != null)
                WHERE {col2...} LIKE {end}
            -- The NOT EXISTS clause ensures this only runs if the LIKE query has no result
            WHERE NOT EXISTS (
                SELECT 1
                FROM fts_result
                WHERE fts_result.question_head LIKE '%g J%'
            )
        )
        ORDER BY relevance DESC;
    */

    private static String buildTableKeys(String[] tableKeysList) {
        StringBuilder tableKeys = new StringBuilder(" ");
        for (String tableKey : tableKeysList) {
            String[] tableKeySplit = tableKey.split(DBOperation.TABLE_KEY_DELIMITER);

            tableKeys.append(buildTableKeys(tableKeySplit[0], tableKeySplit[1].split(DBOperation.KEYS_DELIMITER)));
            tableKeys.append(DBOperation.KEYS_DELIMITER);
        }
        tableKeys.deleteCharAt(tableKeys.length() - 1).append(" ");
        return tableKeys.toString();
    }

    private static void appendMatchAgainst(StringBuilder query, String table, String[] ftsKeys) {
        query.append(" MATCH(");
        for (String ftsKey : ftsKeys) {
            query.append(table).append(".").append(ftsKey).append(DBOperation.KEYS_DELIMITER);
        }
        query.deleteCharAt(query.length() - 1).append(") AGAINST (?) ");
    }

    private static String buildTableKeys(String table, String[] keys) {
        StringBuilder tableKeys = new StringBuilder(" ");
        for (String key : keys) {
            tableKeys.append(table).append(".").append(key).append(DBOperation.KEYS_DELIMITER);
        }
        tableKeys.deleteCharAt(tableKeys.length() - 1).append(" ");
        return tableKeys.toString();
    }

    private static void appendWhereClause(StringBuilder query, String whereCondition, String replaceTable, List<String> parameterValues) {
        if (whereCondition == null) return;
        query.append(" WHERE ");

        while (whereCondition.contains(PARAMETER_VALUE)) {
            parameterValues.add(getParameterValue(whereCondition));
            whereCondition = removeParameterValue(whereCondition);
        }

        if (replaceTable != null) {
            whereCondition = whereCondition.substring(whereCondition.indexOf("."));
            query.append(replaceTable);
        }

        query.append(whereCondition).append(" ");
    }

    private static DBDataObject[] executeQuery(StringBuilder query, List<String> parameterValues) {
        DBDataObject[] dbObjects = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            // Set parameters
            for (int i = 0; i < parameterValues.size(); i++) {
                query.replace(query.indexOf("?"), query.indexOf("?") + 1, parameterValues.get(i));
                preparedStatement.setString(i + 1, parameterValues.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.last();
                dbObjects = new DBDataObject[resultSet.getRow()];
                resultSet.beforeFirst();

                int dbPtr = 0;
                while (resultSet.next()) {
                    dbObjects[dbPtr] = new DBDataObject();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        dbObjects[dbPtr].putKeyValue(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                    }
                    dbPtr++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (PRINT_QUERY) {
            DEBUG.printlnBlue("QUERY: " + query);
            System.out.println(Arrays.toString(dbObjects));
        }

        return dbObjects;
    }

    public static DBDataObject[] ftsLikeSearch(String table, String[] keys, String[] ftsKeys, String searchText, String likeCondition) {
        if (table == null || keys == null || keys.length == 0 || ftsKeys == null || ftsKeys.length == 0) return null;

        StringBuilder query = new StringBuilder("WITH fts_result AS ( ");

        List<String> parameterValues = new ArrayList<>();

        // SELECT
        query.append("SELECT ").append(buildTableKeys(table, keys)).append(DBOperation.KEYS_DELIMITER);
        appendMatchAgainst(query, table, ftsKeys);
        parameterValues.add(searchText);

        // FROM and WHERE
        query.append(" AS relevance_score FROM ").append(table).append(" WHERE ");
        appendMatchAgainst(query, table, ftsKeys);
        parameterValues.add(searchText);
        query.append(") ");

        // first SELECT from fts_result with LIKE condition
        query.append("( SELECT * FROM fts_result ");
        appendWhereClause(query, likeCondition, "fts_result", parameterValues);
        query.append(") ");

        // UNION with NOT EXISTS
        query.append("UNION ( SELECT * FROM fts_result WHERE NOT EXISTS ( SELECT * FROM fts_result ");
        appendWhereClause(query, likeCondition, "fts_result", parameterValues);
        query.append(") ) ORDER BY relevance_score DESC ");

        return executeQuery(query, parameterValues);
    }

    private static void appendCase(StringBuilder query, String condition, String onConditionTrue, List<String> parameterValues) {
        if (condition == null) {
            query.append(" ELSE ").append(onConditionTrue).append(" ");
            return;
        }

        while (condition.contains(PARAMETER_VALUE)) {
            parameterValues.add(getParameterValue(condition));
            condition = removeParameterValue(condition);
        }
        query.append(" WHEN ").append(condition).append(" THEN ").append(onConditionTrue).append(" ");
    }

    public static DBDataObject[] likeRelevanceSearch(String table, String[] keys, String orderByKey, String... likeConditions) {
        if (table == null || keys == null || keys.length == 0 || likeConditions == null || likeConditions.length == 0)
            return null;

        StringBuilder query = new StringBuilder();

        List<String> parameterValues = new ArrayList<>();

        // SELECT
        query.append("SELECT ").append(buildTableKeys(table, keys)).append(DBOperation.KEYS_DELIMITER).append("CASE ");
        for (int i = 0; i < likeConditions.length; i++) {
            appendCase(query, likeConditions[i], String.valueOf(i), parameterValues);
        }

        boolean excludeRemaining = likeConditions[likeConditions.length - 1] == null;
        if (!excludeRemaining)
            appendCase(query, null, String.valueOf(likeConditions.length), parameterValues);

        query.append(" END AS relevance_score FROM ").append(table);

        if (excludeRemaining) {
            query.append(" HAVING relevance_score NOT IN (").append(likeConditions.length - 1).append(")");
        }

        query.append(" ORDER BY relevance_score ");

        if (orderByKey != null)
            query.append(", ").append(table).append(".").append(orderByKey);

        return executeQuery(query, parameterValues);
    }
    /*
        SELECT tag_id, tag,
            CASE
            WHEN tag LIKE CONCAT(?, '%') THEN 1  -- Exact match or starts with
            WHEN tag LIKE CONCAT('%', ?, '%') THEN 2  -- Contains
            ELSE 3  -- No match
            END AS relevance_score
        FROM qna_tags
        ORDER BY relevance_score, tag;
*/

    public static void main(String[] args) {
        init();
        PRINT_QUERY = true;
        //DBDataObject[] d = DBQuestion.searchByQuestionHead("What is Java", DBQuestion.K_QUESTION_HEAD, DBQuestion.K_QUESTION_BODY);
        DBDataObject[] d2 = likeRelevanceSearch(DBTag.TABLE, new String[]{DBTag.K_TAG}, DBTag.K_TAG,
                DBTag.ops.likeMatchStartsWith(DBTag.K_TAG, "Op"),
                DBTag.ops.likeMatchContains(DBTag.K_TAG, "Op"));
        close();
    }

    // selectKeysFromTables = "table1:key1,key3", "table3:*", "table2:key3"
    // whereCondition/onCondition = "table1.key1 = table2.key2" (explicitValue == "table1.key1 = <?P_V?>'this is a value'<?P_V?>")

    // SELECT COUNT(*) FROM TABLE WHERE SOMETHING = ?
    public static int count(String table, String key, String condition) {
        StringBuilder query = new StringBuilder("SELECT COUNT(")
                .append(key).append(") FROM ").append(table).append(" WHERE ");

        String parameterValue = null;

        if (condition.contains(PARAMETER_VALUE)) {
            parameterValue = getParameterValue(condition);
            condition = removeParameterValue(condition);
        }

        query.append(condition);

        String retrievedValue = "0";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            if (parameterValue != null)
                preparedStatement.setString(1, parameterValue);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    retrievedValue = resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (PRINT_QUERY)
            DEBUG.printlnBlue("GENERATE_QUERY: " + query + " => " + retrievedValue);

        return Integer.parseInt(retrievedValue);
    }

    public static void update(String table, String[] updateKeysByNewValues, String condition) {
        StringBuilder query = new StringBuilder("UPDATE ").append(table).append(" SET ");

        Map<Integer, String> parameterValues = new HashMap<>();
        int paramIndex = 0;
        for (; paramIndex < updateKeysByNewValues.length; paramIndex++) {
            String[] keyValue = updateKeysByNewValues[paramIndex].split(DBOperation.KEY_VALUE_DELIMITER);

            parameterValues.put(paramIndex, getParameterValue(keyValue[1]));
            query.append(keyValue[0]).append(" = ").append("?").append(DBOperation.KEYS_DELIMITER).append(" ");
        }

        query.deleteCharAt(query.length() - 2);

        if (condition != null) {
            query.append("WHERE ");
            while (condition.contains(PARAMETER_VALUE)) {
                parameterValues.put(paramIndex++, getParameterValue(condition));
                condition = removeParameterValue(condition);
            }
            query.append(condition);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {

            for (int i : parameterValues.keySet()) {
                query.replace(query.indexOf("?"), query.indexOf("?") + 1, parameterValues.get(i));
                preparedStatement.setString(i + 1, parameterValues.get(i));
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (PRINT_QUERY)
            DEBUG.printlnBlue(("QUERY: " + query));
    }

    public static String add(String table, String[] keys, String[] values, String whereCondition) {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(table).append(" (");
        String[] parameterValues = new String[values.length + (whereCondition != null ? 1 : 0)];

        for (String key : keys) {
            query.append(key).append(DBOperation.KEYS_DELIMITER);
        }

        query.deleteCharAt(query.length() - 1).append(") VALUES (");

        for (int i = 0; i < values.length; i++) {
            parameterValues[i] = values[i];
            query.append("?").append(DBOperation.KEYS_DELIMITER);
        }

        query.deleteCharAt(query.length() - 1).append(") ");

        if (whereCondition != null) {
            query.append("WHERE ").append(removeParameterValue(whereCondition));
            parameterValues[parameterValues.length - 1] = getParameterValue(whereCondition);
        }

        String generatedKey = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < parameterValues.length; i++) {
                query.replace(query.indexOf("?"), query.indexOf("?") + 1, parameterValues[i]);
                preparedStatement.setString(i + 1, parameterValues[i]);
            }

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeysSet = preparedStatement.getGeneratedKeys()) {

                if (generatedKeysSet.next()) {
                    generatedKey = String.valueOf(generatedKeysSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (PRINT_QUERY)
            DEBUG.printlnBlue(("QUERY: " + query));
        return generatedKey;
    }

    public static DBDataObject[] getTop(String[] selectKeysFromTables, String[] onCondition, String whereCondition, String aggregateKeyFunc, String additionalOrderBy, String limit) {
        if (selectKeysFromTables == null || selectKeysFromTables.length == 0 ||
                (onCondition != null && onCondition.length + 1 != selectKeysFromTables.length)) return null;

        StringBuilder query = new StringBuilder("SELECT ");

        Map<Integer, String> parameterValues = new HashMap<>();
        int lastParamIndex = 0;

        String[] firstTable = selectKeysFromTables[0].split(DBOperation.TABLE_KEY_DELIMITER);

        // append select
        for (String selectKeysFromTable : selectKeysFromTables) {
            String[] tableKey = selectKeysFromTable.split(DBOperation.TABLE_KEY_DELIMITER);

            if (tableKey[1].trim().isEmpty())
                continue;

            String[] keys = tableKey[1].split(DBOperation.KEYS_DELIMITER);

            for (String key : keys) {
                query.append(tableKey[0]).append(".").append(key).append(DBOperation.KEYS_DELIMITER);
            }
        }

        // append aggregate function
        // example aggregateKeyFunc -> COUNT(table.key) | MAX(table.key)...
        String aggregateName = firstTable[0] + "_" + aggregateKeyFunc.split(" ")[0];//aggregateKeyFunc.split(DBOperation.AGGREGATE_DELIMITER)[0];
        String aggregateFunction = aggregateKeyFunc.replace(" ", ""); //aggregateKeyFunc.split(DBOperation.AGGREGATE_DELIMITER)[1];
        query.append(aggregateFunction).append(" as ").append(aggregateName).append(" "); //as a_f ");

        // append from
        query.append("FROM ").append(firstTable[0]).append(" ");

        // join other tables
        for (int i = 1; i < selectKeysFromTables.length; i++) {
            String[] tableKey = selectKeysFromTables[i].split(DBOperation.TABLE_KEY_DELIMITER);
            query.append("JOIN ").append(tableKey[0]).append(" ");

            if (onCondition != null && onCondition[i - 1] != null) {

                String condition = onCondition[i - 1];

                if (condition.contains(PARAMETER_VALUE)) {
                    parameterValues.put(i, getParameterValue(condition));
                    lastParamIndex = i;
                    condition = removeParameterValue(condition);
                }
                query.append("ON ").append(condition).append(" ");
            }
        }

        // append where
        if (whereCondition != null) {
            query.append("WHERE ");

            while (whereCondition.contains(PARAMETER_VALUE)) {
                parameterValues.put(++lastParamIndex, getParameterValue(whereCondition));
                whereCondition = removeParameterValue(whereCondition);
            }

            query.append(whereCondition).append(" ");
        }

        // append group by
        query.append("GROUP BY ");
        for (String selectKeysFromTable : selectKeysFromTables) {
            String[] tableKey = selectKeysFromTable.split(DBOperation.TABLE_KEY_DELIMITER);

            if (tableKey[1].trim().isEmpty())
                continue;

            String[] keys = tableKey[1].split(DBOperation.KEYS_DELIMITER);

            for (String key : keys) {
                query.append(tableKey[0]).append(".").append(key).append(DBOperation.KEYS_DELIMITER);
            }
        }
        query.deleteCharAt(query.length() - 1).append(" ");

        // append order by
        String[] additionalOrderByTableKeys = additionalOrderBy.split(DBOperation.TABLE_KEY_DELIMITER);
        additionalOrderBy = additionalOrderByTableKeys[0] + "." + additionalOrderByTableKeys[1].split(DBOperation.KEYS_DELIMITER)[0];
        query.append(" ORDER BY ").append(aggregateName).append(" DESC,").append(additionalOrderBy).append(" ASC");

        if (limit != null)
            query.append(" LIMIT ").append(limit);

        DBDataObject[] dbObjects = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            for (int i : parameterValues.keySet()) {
                query.replace(query.indexOf("?"), query.indexOf("?") + 1, parameterValues.get(i));
                preparedStatement.setString(i, parameterValues.get(i));
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();

                resultSet.last();
                dbObjects = new DBDataObject[resultSet.getRow()];
                for (int i = 0; i < dbObjects.length; i++) {
                    dbObjects[i] = new DBDataObject();
                }

                resultSet.beforeFirst();

                int dbPtr = 0;
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        dbObjects[dbPtr].putKeyValue(metaData.getColumnName(i), resultSet.getString(i));
                    }
                    dbPtr++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (PRINT_QUERY)
            DEBUG.printlnBlue(("QUERY: " + query));
        //System.out.println(Arrays.toString(dbObjects));
        return dbObjects;
    }

    public static DBDataObject[] get(String[] selectKeysFromTables, String[] onCondition, String whereCondition, boolean isDistinct) {
        if (selectKeysFromTables == null || selectKeysFromTables.length == 0 ||
                (onCondition != null && onCondition.length + 1 != selectKeysFromTables.length)) return null;

        StringBuilder query = new StringBuilder("SELECT ").append(isDistinct ? "DISTINCT " : "");

        Map<Integer, String> parameterValues = new HashMap<>();
        int lastParamIndex = 0;

        String[] firstTable = selectKeysFromTables[0].split(DBOperation.TABLE_KEY_DELIMITER);

        // append select
        for (String selectKeysFromTable : selectKeysFromTables) {
            String[] tableKey = selectKeysFromTable.split(DBOperation.TABLE_KEY_DELIMITER);

            if (tableKey[1].trim().isEmpty())
                continue;

            String[] keys = tableKey[1].split(DBOperation.KEYS_DELIMITER);

            for (String key : keys) {
                query.append(tableKey[0]).append(".").append(key).append(DBOperation.KEYS_DELIMITER);
            }
        }
        query.deleteCharAt(query.length() - 1).append(" ");

        // append from
        query.append("FROM ").append(firstTable[0]).append(" ");

        // join other tables
        for (int i = 1; i < selectKeysFromTables.length; i++) {
            String[] tableKey = selectKeysFromTables[i].split(DBOperation.TABLE_KEY_DELIMITER);

            query.append("JOIN ").append(tableKey[0]).append(" ");

            if (onCondition != null && onCondition[i - 1] != null) {

                String condition = onCondition[i - 1];

                if (condition.contains(PARAMETER_VALUE)) {
                    parameterValues.put(i, getParameterValue(condition));
                    lastParamIndex = i;
                    condition = removeParameterValue(condition);
                }
                query.append("ON ").append(condition).append(" ");
            }
        }

        if (whereCondition != null) {
            query.append("WHERE ");

            while (whereCondition.contains(PARAMETER_VALUE)) {
                parameterValues.put(++lastParamIndex, getParameterValue(whereCondition));
                whereCondition = removeParameterValue(whereCondition);
            }

            query.append(whereCondition);
        }

        DBDataObject[] dbObjects = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString(),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            for (int i : parameterValues.keySet()) {
                query.replace(query.indexOf("?"), query.indexOf("?") + 1, parameterValues.get(i));
                preparedStatement.setString(i, parameterValues.get(i));
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();

                resultSet.last();
                dbObjects = new DBDataObject[resultSet.getRow()];
                for (int i = 0; i < dbObjects.length; i++) {
                    dbObjects[i] = new DBDataObject();
                }

                resultSet.beforeFirst();

                int dbPtr = 0;
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        dbObjects[dbPtr].putKeyValue(metaData.getColumnName(i), resultSet.getString(i));
                    }
                    dbPtr++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (PRINT_QUERY)
            DEBUG.printlnBlue(("QUERY: " + query));
        // System.out.println(Arrays.toString(dbObjects));
        return dbObjects;
    }

    private static String removeParameterValue(String con) {
        int firstIndex = con.indexOf(PARAMETER_VALUE);
        int secondIndex = con.indexOf(PARAMETER_VALUE, firstIndex + PARAMETER_VALUE.length());

        return con.substring(0, firstIndex) + "?" + con.substring(secondIndex + PARAMETER_VALUE.length());
    }

    private static String getParameterValue(String condition) {
        int start = condition.indexOf(PARAMETER_VALUE) + PARAMETER_VALUE.length();
        int end = condition.indexOf(PARAMETER_VALUE, start);

        return condition.substring(start, end);
    }
}
