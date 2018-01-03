package net;

import controller.Controller;

import javax.ejb.EJB;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@ServerEndpoint(value = "/message")
public class MessageHandler {
    @EJB
    private Controller controller = new Controller();
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private String[] variation = {" guessed ", " thoughtfully proposed ", " suggested that the word might be ", " believed the word to be ", " speculates the word to be "};

    @OnOpen
    public void onOpen(Session peer) {
        //controller.increaseNoP();
        if(peers.isEmpty()) {
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
                if(extractedMsg.getMessage().equals("name")) {
                    peer.getUserProperties().put("username", extractedMsg.getSender());
                    for (Session session : peers) {
                        if (session.isOpen()) {
                            try {
                                session.getBasicRemote().sendText(MessageEncoder.encode(extractedMsg.getSender() + " joined the room! "/* + controller.getNumberOfPlayers() + " people are currently here." */));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else
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
        /*controller.decreaseNoP();
        for (Session session : peers) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(MessageEncoder.encode(peer.getUserProperties().get("username") + " leaved the room! " + controller.getNumberOfPlayers() + " people are currently here." ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    @OnError
    public void error(Session peer, Throwable error) {
        error.printStackTrace();
    }

    private void guessWord(String guess, Session peer) {
        boolean right = false;
        if(controller.guessWord(guess)) {
            right = true;
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, variation.length + 1);
        for (Session session : peers) {
            if (session.isOpen()) {
                try {
                    if(right)
                        session.getBasicRemote().sendText(MessageEncoder.encode(peer.getUserProperties().get("username") + variation[randomNum] + guess
                                                        + " and it was the right answer! The current score is " + controller.getScore() + " points."));
                    else
                        session.getBasicRemote().sendText(MessageEncoder.encode(peer.getUserProperties().get("username") + variation[randomNum] + guess
                                                        + ". Sadly it was the wrong answer :("));
                    session.getBasicRemote().sendText(MessageEncoder.encode(new Message("gameMaster", "server", "The word you should explain is: " + controller.getWord())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
