package net;

import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint(value = "/message"/*, encoders = { MessageEncoder.class }, decoders = { MessageDecoder.class } */)
public class MessageHandler {
    private Gson gson = new Gson();
    Logger logger = Logger.getLogger(getClass().getName());

    @OnOpen
    public void open(Session session) {
        try {
            System.out.println("Opened");
            logger.severe("Opened");
            session.getUserProperties().put("sessionUser", "temp");
            session.getBasicRemote().sendText("all:::Starting my connection at with username temp and id: " + session.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            String[] splitMessage = msg.split(":::");
            System.out.println(splitMessage[1]);
            String option = splitMessage[0].toLowerCase();
            Message extractedMsg = gson.fromJson(splitMessage[1], Message.class);
            switch(option) {
                case "send":
                    String sender = (String) session.getUserProperties().get("sessionUser");
                    if(sender.equals("temp"))
                        session.getUserProperties().replace("sessionUser", extractedMsg.getSender());
                    else {
                        for (Session sess : session.getOpenSessions()) {
                            String receiver = (String) sess.getUserProperties().get("sessionUser");
                            if (receiver.equals(extractedMsg.getReceiver()) && sess.isOpen()) {
                                sess.getBasicRemote().sendText(splitMessage[1]);
                            }
                        }
                    }
                    break;

                case "all":
                    for (Session sess : session.getOpenSessions()) {
                        if (sess.isOpen()) {
                            sess.getBasicRemote().sendText(splitMessage[1]);
                        }
                    }
                    break;

                default:
                    System.err.println("Couldn't find option, went to default.");
                    break;
            }

        } catch (IOException e) {
            System.err.println("Couldn't send message...");
            e.printStackTrace();
        } /*catch (EncodeException e) {
            e.printStackTrace();
        }*/

    }

    @OnError
    public void error(Session session, Throwable error) {

    }

    @OnClose
    public void close(Session session, CloseReason reason) {

    }
}
