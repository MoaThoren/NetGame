package net;

public final class MessageEncoder {
    private static String ENCODING_DELIMITER = ";;;";

    public static String encode(String msg) {
        return "all" + ENCODING_DELIMITER + "server" + ENCODING_DELIMITER + msg;
    }

    public static String encode(Message msg) {
        return msg.getReceiver() + ENCODING_DELIMITER + msg.getSender() + ENCODING_DELIMITER + msg.getMessage();
    }

    public static Message decode(String string) {
        String[] message = string.split(ENCODING_DELIMITER);
        return new Message(message[0], message[1], message[2]);
    }

    public static boolean willDecode(String string) {
        return string.contains(ENCODING_DELIMITER);
    }
}
