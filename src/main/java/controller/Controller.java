package controller;

import model.Game;

import java.util.logging.Logger;

public class Controller {
    Logger logger = Logger.getLogger(getClass().getName());
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
        logger.warning(game.getWord());
        return game.getWord();
    }
}
