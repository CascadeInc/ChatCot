package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.ConnectorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Class for working with DataBase (low level)
 */
public class MySqlUtil extends SqlUtil {
    private static final Logger LOGGER = LogManager.getLogger(MySqlUtil.class);

    // URL of connection on MySQL server    CHANGE TO YOUR
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String PATH = "db.properties";


    public MySqlUtil() {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            if (connection != null) {
                LOGGER.info("Connection successfully");
            }
            else {
                LOGGER.info("Connection not successfully");
            }
        }
        catch (SQLException e) {
            LOGGER.error("Connection failed - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * executing SQL request for getting result
     * @param sqlRequest - SQL request
     * @return - result of request
     */
    @Override
    public ResultSet exec(String sqlRequest) {
        LOGGER.info("executing MySQL query = \"" + sqlRequest + "\"....");
        return super.exec(sqlRequest);
    }

    @Override
    public ResultSet execPrepare(String sqlRequest, String... strings) {
        LOGGER.info("executing MySQL prepare query = \"" + sqlRequest + "\"....");
        return super.execPrepare(sqlRequest, strings);
    }

    /**
     * executing SQL request for setting and appending data
     * @param sqlRequest - SQL request for executing
     * @return - nothing
     */
    @Override
    public int execUpdate(String sqlRequest) {
        LOGGER.info("executing MySQL query = \"" + sqlRequest + "\"....");
        return super.execUpdate(sqlRequest);
    }

    /**
     * close connection with DataBase
     */
    public void shutdown() {
        try {
            LOGGER.info("closing MySQL connection to DataBase....");
            connection.close();
            LOGGER.info("MySQL connection was closed");
            ConnectionPool.getInstance().releaseConnection((ProxyConnection) connection);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("can't close MySQL connection to DataBase");
        }
    }
}
