package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

abstract public class SqlUtil {
    private final static Logger LOGGER = LogManager.getLogger(SqlUtil.class);
    static Connection connection;

    /**
     * executing SQL request for getting result
     * @param sqlRequest - SQL request
     * @return - result of request
     */
    public ResultSet exec(String sqlRequest) throws DataBaseException {
        try {
            if (connection.isClosed()) {
                throw new DataBaseException("closed connection");
            }
            Statement statement = connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(sqlRequest);
        }
        catch (SQLException e) {
            LOGGER.fatal("can't execute: \"" + sqlRequest + "\"");
            LOGGER.catching(e);
        }
        return null;
    }

    /**
     * executing SQL request for setting and appending data
     * @param sqlRequest - SQL request for executing
     * @return - nothing
     */
    public int execUpdate(String sqlRequest) throws DataBaseException {
        try {
            if (connection.isClosed()) {
                throw new DataBaseException("closed connection");
            }
            Statement statement = connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeUpdate(sqlRequest);
        }
        catch (SQLException e) {
            LOGGER.fatal("can't execute: \"" + sqlRequest + "\"");
            LOGGER.catching(e);
        }
        return -1;
    }

    public ResultSet execPrepare(String sqlRequest, String... strings) throws DataBaseException {
        try {
            if (connection.isClosed()) {
                throw new DataBaseException("closed connection");
            }
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
            StringBuilder builder = new StringBuilder();
            for (String s : strings) {
                builder.append(s);
                builder.append("; ");
            }
            LOGGER.fatal("can't execute prepare statement = \"" + sqlRequest + "\" with args = [" + builder.toString() + "]");
            LOGGER.catching(e);
        }
        return null;
    }
}
