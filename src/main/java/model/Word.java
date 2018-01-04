package model;

import common.WordDTO;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(
                name = "fetchWords",
                query = "SELECT words FROM words words WHERE words.difficulty LIKE :difficulty"
        ),
        @NamedQuery(
                name = "fetchWord",
                query = "SELECT words FROM words words WHERE words.difficulty LIKE :difficulty"
        ),
        @NamedQuery(
                name = "length",
                query = "SELECT COUNT(word) AS cnt FROM words"
        )
})

@Entity(name = "words")
public class Word implements WordDTO, Serializable {
    @Id
    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Version
    @Column(name = "version")
    private int versionNum;

    public Word() {}

    public Word(String word, String difficulty) {
        this.word = word;
        this.difficulty = difficulty;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * @return A string representation of all fields in this object.
     */
    @Override
    public String toString() {
        return "Word: " + word + " with a difficulty of: " + difficulty;
    }
}
