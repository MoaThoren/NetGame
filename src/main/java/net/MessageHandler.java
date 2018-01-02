package net;

import controller.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/message")
public class MessageHandler {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private Controller controller;

    @OnOpen
    public void onOpen(Session peer) {
        if(peers.isEmpty()) {
            controller  = new Controller();
            peer.getUserProperties().put("isFirst", true);
        } else
            peer.getUserProperties().put("isFirst", false);
        peers.add(peer);
        try {
            if(!(boolean) peer.getUserProperties().get("isFirst"))
                peer.getBasicRemote().sendText(MessageEncoder.encode("Welcome to the chat!\nYou are a minion"));
            else {
                peer.getBasicRemote().sendText(MessageEncoder.encode("Welcome to the chat!\nYou are the game master"));
                peer.getBasicRemote().sendText(MessageEncoder.encode("The word you should explain is: " + controller.getWord()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String msg, Session peer) {
        try {
            Message extractedMsg = MessageEncoder.decode(msg);
            if(extractedMsg.getReceiver().equals("server")) {
                if(extractedMsg.getMessage().equals("name"))
                    peer.getUserProperties().put("username", extractedMsg.getSender());
                else
                    guessWord(extractedMsg.getMessage(), peer);
            } else {
                for (Session session : peers) {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(MessageEncoder.encode(extractedMsg));
                    }
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

    private void guessWord(String guess, Session peer) {
        String right = "wrong";
        if(controller.guessWord(guess))
            right = "right";
        for (Session session : peers) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(MessageEncoder.encode(peer.getUserProperties().get("username") + " guessed: " + guess
                                                                                                                + "It was the " + right + " answer."));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
