package by.cascade.chatcot.storage;

import java.sql.SQLClientInfoException;

public class ConnectorInfoException extends SQLClientInfoException {

    public ConnectorInfoException(String message, SQLClientInfoException e) {
        super(message, e.getFailedProperties());
    }

    public ConnectorInfoException(SQLClientInfoException e) {
        super(e.getFailedProperties());
    }
}
