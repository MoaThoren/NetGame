package view;

import net.Chatmessage;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Startup
@Singleton
@Named("messageHistory")
public class MessageHistory {
    private List<Chatmessage> chatmessages = new ArrayList<>();
    private String message = "";
    private String otherMessage = "";
    Logger logger = Logger.getLogger(getClass().getName());

    public List<Chatmessage> getChatmessages() {
        return chatmessages;
    }

    public void addChatmessage() {
        logger.severe("Opened");
        chatmessages.add(new Chatmessage("self", message));
    }

    public void addOtherChatmessage() {
        chatmessages.add(new Chatmessage("other", otherMessage));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
