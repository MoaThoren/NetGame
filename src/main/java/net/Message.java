package net;

public class Message {
    private String sender;
    private String receiver;
    private String message;

    Message(String receiver, String sender, String message) {
        this.sender = sender;
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
        return String.format("(MESSAGE: receiver=%s, sender=%s, message=%s*)", receiver, sender, message);
    }
}
