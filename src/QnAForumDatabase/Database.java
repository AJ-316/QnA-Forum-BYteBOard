/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QnAForumDatabase;

import DataObjects.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * @author AJ
 */
public class Database {

    public static void init() {
        DatabaseHandler.establishConnection("byteboarddb_qnaforum");
    }

    public static void updateData(DataObject data, String matchCol, String matchVal) {
        if (data.get(matchCol) == null) return;
        DatabaseHandler.update(data.table(), data.keys(), data.values(), matchCol, matchVal);
    }

    public static String insertData(DataObject data) {
        return Long.toString(DatabaseHandler.insert(data.table(), data.keys(), data.values()));
    }

    public static DataObject[] getData(String table, String conditionCol, String conditionVal) {
        return DatabaseHandler.selectRow(table, conditionCol, conditionVal);
    }

    public static String[] getData(String table, String col, String conditionCol, String conditionVal, boolean removeDuplicate) {
        return DatabaseHandler.selectRow(table, col, conditionCol, conditionVal, removeDuplicate);
    }

    public static String[] getData(String table, String col, boolean removeDuplicate) {
        return DatabaseHandler.selectRow(table, col, removeDuplicate);
    }

    public static String[] getTags() {
        DataObject[] tagObjects = DatabaseHandler.selectRow(TagDataObject.TABLE);

        ArrayList<String> tags = new ArrayList<>();
        for (DataObject tagObject : tagObjects) {
            String tag = tagObject.get(TagDataObject.tagKey());
            if (!tags.contains(tag))
                tags.add(tag);
        }
        return tags.toArray(new String[0]);
    }

    private static DataObject[] parseDataObjects(ArrayList<String> tableRows, String parseTable) {
        if (tableRows == null) return null;

        int colCount = DatabaseHandler.countColumns(parseTable);
        if (colCount == 0) return null; // Safeguard against division by zero

        DataObject[] dataObjects = new DataObject[(tableRows.size() + colCount - 1) / colCount]; // Ensure rounding up
        int dataObjectCounter = 0;

        for (int i = 0; i < tableRows.size(); i++) {
            String data = tableRows.get(i);
            String[] keyValue = data.split("=", 2); // Limit split parts to 2 for safety
            if (keyValue.length != 2) {
                continue; // Skip malformed data entries
            }

            if (dataObjects[dataObjectCounter] == null) {
                dataObjects[dataObjectCounter] = createDataObject(parseTable);
            }

            dataObjects[dataObjectCounter].put(keyValue[0], keyValue[1]);

            if ((i + 1) % colCount == 0 && i != 0) {
                dataObjectCounter++; // Move to the next DataObject after filling one completely
            }
        }
        return dataObjects;
    }

    private static DataObject createDataObject(String parseTable) {
        switch (parseTable) {
            case UserDataObject.TABLE:
                return new UserDataObject();
            case QuestionDataObject.TABLE:
                return new QuestionDataObject();
            case AnswerDataObject.TABLE:
                return new AnswerDataObject();
            default:
                return new DataObject(parseTable);
        }
    }


    public static boolean isUsernameAvailable(String username) {
        return DatabaseHandler.count(UserDataObject.TABLE, "username", username) == 0;
    }

    public static UserDataObject getUserInfo(String emailOrUsername, boolean isEmail) {
        if (isEmail) {
            if (DatabaseHandler.count(UserDataObject.TABLE, "email", emailOrUsername) == 0)
                return null;
            DataObject userData = DatabaseHandler.selectRow(UserDataObject.TABLE, "email", emailOrUsername)[0];
            return (UserDataObject) userData;
        }

        if (isUsernameAvailable(emailOrUsername))
            return null;

        DataObject userData = DatabaseHandler.selectRow(UserDataObject.TABLE, "username", emailOrUsername)[0];
        return (UserDataObject) userData;
    }

    public static void close() {
        DatabaseHandler.close();
    }

    private static class DatabaseHandler {

        private static Connection connection;

        public static void deleteAll(String table) {
            String query = "DELETE FROM " + table;
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        public static void establishConnection(String database) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", "rootpwd");
                System.out.println("Connected to the database.");
            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
            }
        }

        public static int countColumns(String tableName) {
            int count = 0;
            String query = "SELECT COUNT(*) AS column_count FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, tableName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        count = resultSet.getInt("column_count");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
            }
            return count;
        }

        public static HashMap<String, ArrayList<String>> getData(String table1, String table2, String condition) {
            HashMap<String, ArrayList<String>> result = new HashMap<>();
            StringBuilder queryBuilder = new StringBuilder("SELECT ");

            queryBuilder.append(table1).append(".*, ");
            queryBuilder.append(table2).append(".* ");

            queryBuilder.append("FROM ").append(table1)
                    .append(" JOIN ").append(table2)
                    .append(" ON ").append(table1).append(".username = ").append(table2).append(".username");

            if (condition != null && !condition.isEmpty()) {
                queryBuilder.append(" WHERE ").append(condition);
            }

            String query = queryBuilder.toString();
            //System.out.println(query);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    //System.out.println("next:");
                    for (int i = 1; i <= columnCount; i++) {
                        //System.out.println(metaData.getTableName(i) + ": " + metaData.getColumnName(i) + "=" + resultSet.getString(i));
                        String tableName = metaData.getTableName(i);
                        ArrayList<String> keyValues = result.get(tableName);
                        if (keyValues == null) {
                            keyValues = new ArrayList<>();
                            result.put(tableName, keyValues);
                        }

                        String columnName = metaData.getColumnName(i);
                        String columnValue = resultSet.getString(i);
                        keyValues.add(columnName + "=" + columnValue);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error executing SQL query: " + e.getMessage());
            }

            return result;
        }

        public static void update(String table, String[] cols, String[] vals, String conditionCol, String condtionVal) {
            StringBuilder queryBuilder = new StringBuilder("UPDATE ").append(table).append(" SET ");
            boolean isFirstNonNull = true;
            for (int i = 0; i < cols.length; i++) {
                if (vals[i] != null) {
                    if (!isFirstNonNull) {
                        queryBuilder.append(", ");
                    } else {
                        isFirstNonNull = false;
                    }
                    queryBuilder.append(cols[i]).append(" = ?");
                }
            }

            if (conditionCol != null) {
                queryBuilder.append(" WHERE ").append(conditionCol).append("='").append(condtionVal).append("'");
            }

            String query = queryBuilder.toString();
            //System.out.println("UPDATEQUERY: " + query);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int parameterIndex = 1;
                for (String val : vals) {
                    if (val != null) {
                        preparedStatement.setString(parameterIndex++, val);
                    }
                }
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Updated " + rowsAffected + " rows in " + table + " table.");
            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
            }
        }

        public static long insert(String table, String[] cols, String[] vals) {

            StringBuilder queryBuilder = new StringBuilder("INSERT into ").append(table).append("(");
            for (int i = 0; i < cols.length; i++) {
                queryBuilder.append(cols[i]);
                if (i < cols.length - 1) {
                    queryBuilder.append(", ");
                }
            }

            queryBuilder.append(") Values(");

            for (int i = 0; i < vals.length; i++) {
                queryBuilder.append("?");
                if (i < vals.length - 1) {
                    queryBuilder.append(", ");
                }
            }
            String query = queryBuilder.append(")").toString();
            //System.out.println(connection + ", " + query);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < vals.length; i++)
                    preparedStatement.setString(i + 1, vals[i]);

                int rowsAffected = preparedStatement.executeUpdate();
                //System.out.println("Inserted " + rowsAffected + " rows into " + table + " table.");

                // Retrieve the auto-generated key
                long generatedKey = 0;
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedKey = generatedKeys.getLong(1);
                    }
                }

                //System.out.println("Generated Key: " + generatedKey);
                return generatedKey;
            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
            }
            return -1;
        }

        public static String[] selectRow(String table, String selectCol, String col, String val, boolean removeDuplicate) {
            String query = "SELECT " + (removeDuplicate ? "DISTINCT " : "") + selectCol + " FROM " + table + " WHERE " + col + " = ?";
            //System.out.println("query: " + query);

            ArrayList<String> values = new ArrayList<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, val);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        values.add(resultSet.getString(selectCol));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return values.toArray(new String[0]);
        }

        public static String[] selectRow(String table, String selectCol, boolean removeDuplicate) {
            String query = "SELECT " + (removeDuplicate ? "DISTINCT " : "") + selectCol + " FROM " + table;
            //System.out.println("query: " + query);

            ArrayList<String> values = new ArrayList<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        values.add(resultSet.getString(selectCol));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return values.toArray(new String[0]);
        }

        public static DataObject[] selectRow(String table) {
            String query = "SELECT * FROM " + table;
            ArrayList<String> valuesList = new ArrayList<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String retrievedValue = metaData.getColumnName(i) + "=" + resultSet.getString(i);
                            valuesList.add(retrievedValue);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return parseDataObjects(valuesList, table);
        }

        public static DataObject[] selectRow(String table, String col, String val) {
            String query = "SELECT * FROM " + table + " WHERE " + col + " = ?";
            //System.out.println("query: " + query);
            ArrayList<String> valuesList = new ArrayList<>();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, val);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String retrievedValue = metaData.getColumnName(i) + "=" + resultSet.getString(i);
                            valuesList.add(retrievedValue);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            //System.out.println("VALUESLISTLLLL: " + Arrays.toString(valuesList.toArray()));
            return parseDataObjects(valuesList, table);
        }

        public static int count(String table, String countCol) {
            String query = "SELECT COUNT( " + countCol + " ) FROM " + table;
            int count = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next())
                        count = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return count;
        }

        public static int count(String table, String countCol, String matchVal) {
            String query = "SELECT COUNT(*) FROM " + table + " WHERE " + countCol + " = ?";
            int count = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, matchVal);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        count = resultSet.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return count;
        }

        public static void printTable(String table) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("Describe " + table)) {

                // Print column names
                while (resultSet.next()) {
                    String columnName = resultSet.getString("Field");
                    //System.out.print(columnName + "\t");
                }
                //System.out.println();

                // Execute query to select all data from table
                try (ResultSet dataResultSet = statement.executeQuery("Select * FROM " + table)) {
                    // Print table data
                    while (dataResultSet.next()) {
                        for (int i = 1; i <= dataResultSet.getMetaData().getColumnCount(); i++) {
                            //System.out.print(dataResultSet.getString(i) + "\t");
                        }
                        //System.out.println();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void close() {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
