package by.cascade.chatcot.storage.databaseprocessing.util;

import java.sql.*;

abstract public class SqlUtil {
    static Connection connection;

    /**
     * executing SQL request for getting result
     * @param sqlRequest - SQL request
     * @return - result of request
     */
    public ResultSet exec(String sqlRequest) {
        try {
            Statement statement = connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(sqlRequest);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * executing SQL request for setting and appending data
     * @param sqlRequest - SQL request for executing
     * @return - nothing
     */
    public int execUpdate(String sqlRequest) {
        try {
            Statement statement = connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeUpdate(sqlRequest);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ResultSet execPrepare(String sqlRequest, String... strings) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.closeOnCompletion();
            for (int i = 0; i < strings.length; i++) {
                statement.setString(i + 1, strings[i]);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return statement.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
