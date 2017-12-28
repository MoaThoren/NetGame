package model;

public class ServerException extends Exception {
    private String message;
    private Exception exception;

    public ServerException(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
