package controller;

import integration.GuessDAO;
import model.ServerException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.Serializable;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller implements Serializable {
    @EJB
    private GuessDAO guessDB = new GuessDAO();

    public String getWord(String difficulty) throws ServerException {
        return guessDB.getWord(difficulty).getWord();
    }
}
