package by.cascade.chatcot.storage.databaseprocessing.util;

import by.cascade.chatcot.storage.ConnectorException;
import by.cascade.chatcot.storage.CreatorException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionCreator {
    private static final String URL = "jdbc:mysql://localhost:3306/musicstore";

    private Properties properties;

    ConnectionCreator(String path) throws CreatorException {
        properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
        }
        catch (FileNotFoundException e) {
            throw new CreatorException("can't find file = \"" + path + "\"", e);
        }
        catch (IOException e) {
            throw new CreatorException("can't read file = \"" + path + "\"", e);
        }
    }

    Connection create() throws ConnectorException {
        try {
            return DriverManager.getConnection(URL, properties);
        } catch (SQLException e) {
            throw new ConnectorException("can't getting connector", e);
        }
    }
}


