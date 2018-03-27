package by.cascade.chatcot.storage;

import java.sql.SQLException;

public class ConnectorException extends SQLException {
    public ConnectorException(String message) {
        super(message);
    }

    public ConnectorException(String message, Exception e) {
        super(message, e);
    }
}
