package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.ConnectorException;
import by.cascade.chatcot.storage.CreatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * singleton for getting connections with DataBase
 */
public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private static Lock instanceLock = new ReentrantLock();
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static final int COUNT_CONNECTIONS = 20;
    private static final String DB_PROPERTIES = "db.properties";
    private ConnectionCreator connectionCreator;

    private ArrayDeque<Connection> connections;
    private Lock lock = new ReentrantLock();

    private ConnectionPool() throws ConnectorException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        }
        catch (SQLException e) {
            throw new ConnectorException("can't register driver", e);
        }
        connections = new ArrayDeque<>(COUNT_CONNECTIONS);
        try {
            connectionCreator = new ConnectionCreator(DB_PROPERTIES);
            for (int i = 0; i < COUNT_CONNECTIONS; i++) {
                connections.push(new ProxyConnection(connectionCreator.create()));
            }
        }
        catch (CreatorException e) {
            LOGGER.catching(e);
            throw new RuntimeException();
        }
    }

    public static ConnectionPool getInstance() throws ConnectorException {
        if (!isCreated.get()) {
            instanceLock.lock();
            if (instance == null) {
                instance = new ConnectionPool();
                isCreated.set(true);
            }
            instanceLock.unlock();
        }
        return instance;
    }

    void releaseConnection(ProxyConnection connection) throws ConnectorException {
        LOGGER.info("RELEASE CONNECTION");
        lock.lock();
        if (!connection.isClosed()) {
            connection.closeConnection();
        }
        ProxyConnection proxyConnection = new ProxyConnection(connectionCreator.create());
        LOGGER.info("adding new connection = " + proxyConnection);
        connections.push(proxyConnection);
        lock.unlock();
        LOGGER.info("COUNT OF CONNECTIONS = " + connections.size());
    }

    public void destroy() throws ConnectorException {
        lock.lock();
        for (Connection connection : connections) {
            ((ProxyConnection)connection).closeConnection();
        }
        lock.unlock();
        try {
            DriverManager.deregisterDriver(new com.mysql.cj.jdbc.Driver());
        }
        catch (SQLException e) {
            throw new ConnectorException("can't deregister driver", e);
        }
    }


    Connection getConnection() {
        LOGGER.info("GET CONNECTION");
        lock.lock();
        Connection connection = connections.pop();
        LOGGER.info("getting connection = " + connection);
        lock.unlock();
        LOGGER.info("COUNT OF CONNECTIONS = " + connections.size());
        return connection;
    }

}
