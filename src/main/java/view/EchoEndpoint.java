package view;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/echo", encoders = { MessageEncoder.class })
public class EchoEndpoint {
    @OnOpen
    public void open(Session session, EndpointConfig conf) {

    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            for (Session sess : session.getOpenSessions()) {
                if (sess.isOpen())
                    sess.getBasicRemote().sendObject(msg);
            }
        } catch (IOException e) {
            System.err.println("Couldn't send message...");
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
