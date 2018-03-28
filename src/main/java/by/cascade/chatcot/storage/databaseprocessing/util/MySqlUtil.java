package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.ConnectorException;
import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for working with DataBase (low level)
 */
public class MySqlUtil extends SqlUtil {
    private static final Logger LOGGER = LogManager.getLogger(MySqlUtil.class);

    public MySqlUtil() throws DataBaseException {
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
            throw new DataBaseException("can't getting connection", e);
        }
    }

    /**
     * executing SQL request for getting result
     * @param sqlRequest - SQL request
     * @return - result of request
     */
    @Override
    public ResultSet exec(String sqlRequest) throws DataBaseException {
        LOGGER.info("executing MySQL query = \"" + sqlRequest + "\"....");
        return super.exec(sqlRequest);
    }

    @Override
    public ResultSet execPrepare(String sqlRequest, String... strings) throws DataBaseException {
        LOGGER.info("executing MySQL prepare query = \"" + sqlRequest + "\"....");
        return super.execPrepare(sqlRequest, strings);
    }

    /**
     * executing SQL request for setting and appending data
     * @param sqlRequest - SQL request for executing
     * @return - nothing
     */
    @Override
    public int execUpdate(String sqlRequest) throws DataBaseException {
        LOGGER.info("executing MySQL query = \"" + sqlRequest + "\"....");
        return super.execUpdate(sqlRequest);
    }

    /**
     * close connection with DataBase
     */
    public void shutdown() throws DataBaseException {
        try {
            LOGGER.info("MySQL connection was closed");
            ConnectionPool.getInstance().releaseConnection((ProxyConnection) connection);
        } catch (SQLException e) {
            throw new DataBaseException("can't close connection", e);
        }
    }

    public boolean isOpen() throws ConnectorException {
        try {
            return connection.isClosed();
        }
        catch (SQLException e) {
            throw new ConnectorException("undefined status of connection", e);
        }
    }

    @Override
    public String toString() {
        return connection.toString();
    }
}
