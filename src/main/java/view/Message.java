package view;

public class Message {
    private String name;
    private String message;

    Message(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("(name=%s, message=%s)", name, message);
    }
}
