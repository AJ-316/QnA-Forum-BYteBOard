package DatabasePackage;

import CustomControls.DEBUG;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    public static final String PARAMETER_VALUE = "<?P_V?>";
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

    public static void main(String[] args) {
        init();

        DBQuestion.ops.updateValueBy(
                DBQuestion.ops.matchByValue(DBQuestion.K_QUESTION_HEAD, "H2"),
                DBQuestion.K_QUESTION_HEAD, "Head2", DBQuestion.K_QUESTION_BODY, "Body2");
//        String[] s = get(
//                new String[]{"student:name", "subjects:studentCount"},
//                new String[]{"student.subid = subjects.subid"},
//                "student.name = " + PARAMETER_VALUE + "aj" + PARAMETER_VALUE);
//        System.out.println(Arrays.toString(s));

        //System.out.println(add("student", new String[]{"subid", "name"}, new String[]{"3", "gj"}, "student.subid = " + PARAMETER_VALUE + "1" + PARAMETER_VALUE));
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
            if(parameterValue != null)
                preparedStatement.setString(1, parameterValue);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    retrievedValue = resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DEBUG.printlnBlue("GENERATE_QUERY: " + query + " => " + retrievedValue);

        return Integer.parseInt(retrievedValue);
    }

    public static void update(String table, String[] updateKeysByNewValues, String condition) {
        StringBuilder query = new StringBuilder("UPDATE ").append(table).append(" SET ");

        Map<Integer, String> parameterValues = new HashMap<>();
        int paramIndex = 0;
        for (;paramIndex < updateKeysByNewValues.length; paramIndex++) {
            String[] keyValue = updateKeysByNewValues[paramIndex].split(DBOperation.KEY_VALUE_DELIMITER);

            parameterValues.put(paramIndex, getParameterValue(keyValue[1]));
            query.append(keyValue[0]).append(" = ").append("?").append(DBOperation.KEYS_DELIMITER).append(" ");
        }

        query.deleteCharAt(query.length()-2);

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
                query.replace(query.indexOf("?"), query.indexOf("?")+1, parameterValues.get(i));
                preparedStatement.setString(i+1, parameterValues.get(i));
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DEBUG.printlnBlue(("QUERY: " + query));
    }

    public static String add(String table, String[] keys, String[] values, String whereCondition) {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(table).append(" (");
        String[] parameterValues = new String[values.length + (whereCondition!=null? 1 : 0)];

        for (String key : keys) {
            query.append(key).append(DBOperation.KEYS_DELIMITER);
        }

        query.deleteCharAt(query.length()-1).append(") VALUES (");

        for (int i = 0; i < values.length; i++) {
            parameterValues[i] = values[i];
            query.append("?").append(DBOperation.KEYS_DELIMITER);
        }

        query.deleteCharAt(query.length()-1).append(") ");

        if (whereCondition != null) {
            query.append("WHERE ").append(removeParameterValue(whereCondition));
            parameterValues[parameterValues.length - 1] = getParameterValue(whereCondition);
        }

        String generatedKey = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < parameterValues.length; i++) {
                query.replace(query.indexOf("?"), query.indexOf("?")+1, parameterValues[i]);
                preparedStatement.setString(i+1, parameterValues[i]);
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

        DEBUG.printlnBlue(("QUERY: " + query));
        return generatedKey;
    }

    public static DBDataObject[] get(String[] selectKeysFromTables, String[] onCondition, String whereCondition, boolean isDistinct) {
        if(selectKeysFromTables == null || selectKeysFromTables.length == 0 ||
                (onCondition != null && onCondition.length+1 != selectKeysFromTables.length)) return null;

        StringBuilder query = new StringBuilder("SELECT ").append(isDistinct ? "DISTINCT " : "");

        Map<Integer, String> parameterValues = new HashMap<>();
        int lastParamIndex = 0;

        String[] firstTable = selectKeysFromTables[0].split(DBOperation.TABLE_KEY_DELIMITER);

        // append select
        for (String selectKeysFromTable : selectKeysFromTables) {
            String[] tableKey = selectKeysFromTable.split(DBOperation.TABLE_KEY_DELIMITER);

            if(tableKey[1].trim().isEmpty())
                continue;

            String[] keys = tableKey[1].split(DBOperation.KEYS_DELIMITER);

            for (String key : keys) {
                query.append(tableKey[0]).append(".").append(key).append(DBOperation.KEYS_DELIMITER);
            }
        }
        query.deleteCharAt(query.length()-1).append(" ");

        // append from
        query.append("FROM ").append(firstTable[0]).append(" ");

        // join other tables
        for (int i = 1; i < selectKeysFromTables.length; i++) {
            String[] tableKey = selectKeysFromTables[i].split(DBOperation.TABLE_KEY_DELIMITER);
            query.append("JOIN ").append(tableKey[0]).append(" ");

            if (onCondition != null && onCondition[i - 1] != null) {

                String condition = onCondition[i-1];

                if(condition.contains(PARAMETER_VALUE)) {
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
                query.replace(query.indexOf("?"), query.indexOf("?")+1, parameterValues.get(i));
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
