package metier;


import com.DAO.Quiz;
import com.DAO.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named
@SessionScoped
public class QuizBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public QuizBean() {
    }


    private List<Quiz> userQuizs = new ArrayList<Quiz>();

    private Long quizID;
    private Quiz quiz = new Quiz();
    private User user = new User();
    private String isQuizDeleted;


    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("quiz");
    private EntityManager entityManager;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIsQuizDeleted() {
        return isQuizDeleted;
    }

    public void setIsQuizDeleted(String isQuizDeleted) {
        this.isQuizDeleted = isQuizDeleted;
    }

    public Long getQuizID() {
        return quizID;
    }

    public void setQuizID(Long quizID) {
        this.quizID = quizID;
    }
    public void selectQuizId(long id){
        this.quizID = id;
    }

    public List<Quiz> getUserQuizs() {
        return userQuizs;
    }

    public void setUserQuizs(List<Quiz> userQuizs) {
        this.userQuizs = userQuizs;
    }


    public void retrieveQuizzes(User user) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT q FROM Quiz q WHERE q.user.id = :userId";
            TypedQuery<Quiz> query = entityManager.createQuery(jpql, Quiz.class);
            query.setParameter("userId", user.getId());
            userQuizs = query.getResultList();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error retrieving quizzes"));
        } finally {
            entityManager.close();
        }
    }

    public String addQuiz(User user) {
        entityManager = entityManagerFactory.createEntityManager();
        quiz.setUser(user);
        user.getQuizzes().add(quiz);
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(quiz);
            transaction.commit();
            //retrieve the user's quizzes after adding a new quiz to the database.
            retrieveQuizzes(user);
            quiz = new Quiz();
            return "/views/creatorIndex.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error adding quiz"));
            return null;
        } finally {
            entityManager.close();
        }
    }

    public String deleteQuiz(long quizId,User user) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Quiz quiz = entityManager.find(Quiz.class, quizId);
        if (quiz != null) {
            entityManager.remove(quiz);
            transaction.commit();
            retrieveQuizzes(user);
            this.isQuizDeleted = "Quiz has been deleted successfully!";
        } else {
            transaction.rollback();
            this.isQuizDeleted = "Quiz wasn't deleted!";
        }
        entityManager.close();
        return "/views/creatorIndex.xhtml?faces-redirect=true";
    }
















}
