package by.cascade.chatcot.storage.databaseprocessing;

public class DataBaseException extends Exception {
    public DataBaseException(String message) {
        super(message);
    }
    public DataBaseException(String message, Exception e) {
        super(message, e);
    }
}
