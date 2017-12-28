package view;

import model.ServerException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;

public class EchoEndpoint extends Endpoint {
    @Override
    public void onOpen(final Session session, EndpointConfig config) {
        session.addMessageHandler((MessageHandler.Whole<String>) msg -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                throw new ServerException("Couldn't send message...", e);
            }
        });
    }
}
