package net;

public class Message {
    private String sender;
    private String receiver;
    private String message;

    Message(String name, String receiver, String message) {
        this.sender = name;
        this.receiver = receiver;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("(MESSAGE: sender=%s, receiver=%s, message=%s*)", sender, receiver, message);
    }
}
