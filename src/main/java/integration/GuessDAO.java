package integration;

import common.UserDTO;
import model.ServerException;
import model.Word;
import model.User;
import common.WordDTO;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class GuessDAO {
    @PersistenceContext(unitName = "currconvPU")
    private EntityManager entityManager;

    public List<WordDTO> getWordList(String difficulty) throws ServerException {
        TypedQuery getWords = entityManager.createNamedQuery("fetchWords", Word.class);
        getWords.setParameter("difficulty", difficulty);
        List wordList = getWords.getResultList();
        if(wordList == null)
            throw new ServerException("Error fetching words!");
        return (List<WordDTO>) wordList;
    }

    public void createUser(User user) throws ServerException {
        try {
            entityManager.persist(user);
        }catch (Exception e){
            throw new ServerException("Error creating user...", e);
        }
    }

    public User checkLogin(UserDTO logInDetails) throws ServerException {
        User user;
        try {
            TypedQuery query = entityManager.createNamedQuery("loginUser", User.class);
            query.setParameter("username", logInDetails.getUsername());
            query.setParameter("password", logInDetails.getPassword());
            user = (User) query.getSingleResult();
        } catch (NoResultException noSuchAccount) {
            throw new ServerException("Wrong login details!", noSuchAccount);
        }
        return user;
    }
}
