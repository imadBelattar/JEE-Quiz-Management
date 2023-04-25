package metier;


import com.DAO.Quiz;
import com.DAO.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private boolean islogged;
    private String userLogin;
    private String userPassword;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("quiz");
    private EntityManager em;
    private User user;

    private HashMap<Quiz, Float> passedQuizzes;

    public HashMap<Quiz, Float> getPassedQuizzes() {
        return passedQuizzes;
    }

    public void setPassedQuizzes(HashMap<Quiz, Float> passedQuizzes) {
        this.passedQuizzes = passedQuizzes;
    }

    public UserBean() {
        user = new User();
        islogged = false;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isIslogged() {
        return islogged;
    }

    public void setIslogged(boolean islogged) {
        this.islogged = islogged;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public User retrieveUser(String login, String password) {
         em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.login = :login AND u.password = :password", User.class);
            query.setParameter("login", login);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    public String authentication() {
        String page = null;
           User loggedInUser = retrieveUser(this.userLogin, this.userPassword);
        if (loggedInUser == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", "Invalid login"));
            return page;
        }
        this.user = loggedInUser;
        islogged = true;
        if (user.getRole() == 1) {
            page= "/views/creatorIndex.xhtml?faces-redirect=true";
        } else if (user.getRole() == 2) {
            page = "/views/learnerIndex.xhtml?faces-redirect=true";
        }
        return page;
    }
    public String logOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
    public void isloggedLevel1() {
        if (!islogged) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void isloggedLevel2() {
        if (!islogged) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../../login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String userPassedQuizzes(){
        passedQuizzes = new HashMap<>();
        em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery("SELECT qs.obtainedScore, q FROM Quiz q JOIN q.QuizScores qs WHERE qs.id.userId = :userId", Object[].class);
            query.setParameter("userId", this.user.getId());
            List<Object[]> resultList = query.getResultList();
            for(Object[] result: resultList){
                float score = (Float) result[0];
                Quiz quiz = (Quiz) result[1];
                passedQuizzes.put(quiz, score);
            }
        } catch (NoResultException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error retrieving passed quizzes of the user !"));
            return null;
        } finally {
            em.close();
        }
        return "/views/learnerROLES/marks.xhtml";
    }



// end of class
}
