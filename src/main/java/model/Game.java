package model;

import common.GameDTO;
import integration.WordHandler;

public class Game implements GameDTO{
    private int score = 0;
    private WordHandler wh = new WordHandler();
    private String currentWord = wh.getWord();

    public Game(){
    }

    public boolean guessWord(String guess){
        if(currentWord.equals(guess)){
            score++;
            currentWord = wh.getWord();
            return true;
        }
        return false;
    }

    public int getScore(){
        return score;
    }

    public String getWord(){
        return currentWord;
    }

    public void resetGame(){
        score = 0;
        return;
    }

    public void newWord(){
        currentWord = wh.getWord();
        return;
    }


}
