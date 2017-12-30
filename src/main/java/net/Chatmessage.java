package net;

public class Chatmessage {
    private String id;
    private String txt;

    public Chatmessage(String selfOrOther, String txt) {
        this.id = selfOrOther;
        this.txt = txt;
    }

    public String getId() {
        return id;
    }

    public String getTxt() {
        return txt;
    }
}
