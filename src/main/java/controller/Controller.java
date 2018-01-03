package controller;

import model.Game;

import javax.ejb.Singleton;

@Singleton
public class Controller {
    private Game game = new Game();

    public Game game(){
        return game;
    }

    public boolean guessWord(String guess){
        return game.guessWord(guess);
    }

    public int getScore(){
        return game.getScore();
    }

    public String getWord(){
        return game.getWord();
    }
}
