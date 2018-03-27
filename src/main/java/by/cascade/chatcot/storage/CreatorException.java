package by.cascade.chatcot.storage;

public class CreatorException extends Exception {
    public CreatorException(String message) {
        super(message);
    }

    public CreatorException(String message, Exception e) {
        super(message, e);
    }
}
