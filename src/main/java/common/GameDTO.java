package common;

public interface GameDTO {
    boolean guessWord(String guess);

    int getScore();

    String getWord();

    void resetGame();

    void newWord();
}
