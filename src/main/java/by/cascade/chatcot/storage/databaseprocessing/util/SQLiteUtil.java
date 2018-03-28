package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.databaseprocessing.DataBaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SQLiteUtil extends SqlUtil {
    private static final Logger LOGGER = LogManager.getLogger(SQLiteUtil.class);

    // scheme name on MySQL server          CHANGE TO YOUR
    private static final String scheme = "main";

    // url of connection on MySQL server    CHANGE TO YOUR
    private static final String url = "jdbc:sqlite:phrases.db";

    private static SQLiteUtil instance = null;

    private SQLiteUtil() {
        try {
            LOGGER.info("Connecting to SQLite : (url = " + url +").......");
            connection = DriverManager.getConnection(url);

            LOGGER.info("Connection SQLite successfully");
        }
        catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("Connection SQLite failed - " + e.getMessage());
        }
    }

    /**
     * executing SQL request for getting result
     * @param sqlRequest - SQL request
     * @return - result of request
     */
    @Override
    public ResultSet exec(String sqlRequest) throws DataBaseException {
        LOGGER.info("executing SQLite query = \"" + sqlRequest + "\"....");
        return super.exec(sqlRequest);
    }

    /**
     * executing SQL request for setting and appending data
     * @param sqlRequest - SQL request for executing
     * @return - nothing
     */
    @Override
    public int execUpdate(String sqlRequest) throws DataBaseException {
        LOGGER.info("executing SQLite query = \"" + sqlRequest + "\"....");
        return super.execUpdate(sqlRequest);
    }

    /**
     * close connection with DataBase
     */
    public void shutdown() {
        try {
            LOGGER.info("closing connection to SQLite DataBase....");
            connection.close();
            LOGGER.info("SQLite connection was closed");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("can't close SQLite connection to DataBase");
        }
    }


    /**
     * SingleTone pattern
     * @return one copy of SQLUtil
     */
    public static SQLiteUtil getInstance() {
        if (instance != null) {
            return instance;
        }
        else {
            return new SQLiteUtil();
        }
    }

    public static String getScheme() {
        return scheme;
    }
}
