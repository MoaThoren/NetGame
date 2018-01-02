package net;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/message")
public class MessageHandler {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session peer) {
        peers.add(peer);
        try {
            peer.getBasicRemote().sendText(MessageEncoder.encode("Welcome to the chat!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String msg) {
        try {
            Message extractedMsg = MessageEncoder.decode(msg);
            for (Session session : peers) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(MessageEncoder.encode(extractedMsg));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void close(Session peer, CloseReason reason) {
        peers.remove(peer);
    }

    @OnError
    public void error(Session peer, Throwable error) {
        error.printStackTrace();
    }
}
