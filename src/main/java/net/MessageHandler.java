package net;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/message", encoders = { MessageEncoder.class })
public class MessageHandler {
    private Session opponent1Session = null;
    private Session opponent2Session = null;

    @OnOpen
    public void open(Session session) {
        if(opponent1Session == null)
            opponent1Session = session;
        else
            opponent2Session = session;

        try {
            session.getBasicRemote().sendText("Pelle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            if(msg.contains("all:")) {
                String newMsg = msg.split("all:")[1];
                for (Session sess : session.getOpenSessions()) {
                    if (sess.isOpen())
                        sess.getBasicRemote().sendObject(newMsg);
                }
            } else {
                if(session.getId().equals(opponent1Session.getId()))
                    opponent2Session.getBasicRemote().sendText(msg);
                else
                    opponent1Session.getBasicRemote().sendText(msg);
            }

        } catch (IOException e) {
            System.err.println("Couldn't send message...");
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }

    }

    @OnError
    public void error(Session session, Throwable error) {

    }

    @OnClose
    public void close(Session session, CloseReason reason) {

    }
}
