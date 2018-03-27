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

public class ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private static Lock instanceLock = new ReentrantLock();
    private static AtomicBoolean isCreated = new AtomicBoolean(false);
    private static final int COUNT_CONNECTIONS = 20;
    private static final String DB_PROPERTIES = "db.properties";

    private ArrayDeque<Connection> connections;

    private ConnectionPool() throws ConnectorException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        }
        catch (SQLException e) {
            throw new ConnectorException("can't register driver", e);
        }
        connections = new ArrayDeque<>(COUNT_CONNECTIONS);
        try {
            ConnectionCreator connectionCreator = new ConnectionCreator(DB_PROPERTIES);
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
        if (!connection.getAutoCommit()) {
            connection.setAutoCommit(true);
        }
        connections.push(connection);
    }

    public void destroy() throws ConnectorException {
        for (Connection connection : connections) {
            ((ProxyConnection)connection).closeConnection();
        }
        try {
            DriverManager.deregisterDriver(new com.mysql.cj.jdbc.Driver());
        }
        catch (SQLException e) {
            throw new ConnectorException("can't deregister driver", e);
        }
    }


    Connection getConnection() {
        return connections.pop();
    }

}
