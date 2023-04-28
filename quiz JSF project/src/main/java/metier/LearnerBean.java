package metier;

import com.DAO.Quiz;
import com.DAO.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Named
@SessionScoped
public class LearnerBean implements Serializable {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("quiz");
    private EntityManager entityManager;
    private List<User> creatorUsers;
    private List<Quiz> selectedQuizzes;

    public LearnerBean() {
        creatorUsers = new ArrayList<User>();
    }

    public List<User> getCreatorUsers() {
        return creatorUsers;
    }

    public void setCreatorUsers(List<User> creatorUsers) {
        this.creatorUsers = creatorUsers;
    }

    public List<Quiz> getSelectedQuizzes() {
        return selectedQuizzes;
    }

    public void setSelectedQuizzes(List<Quiz> selectedQuizzes) {
        this.selectedQuizzes = selectedQuizzes;
    }

    public void retrieveCreatorUsers(User userARG){
        entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT u FROM User u where u.role = 1";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            creatorUsers = query.getResultList();
            List<Quiz> creatorQuizzes;
            String jpqll = "SELECT us.id.quizId FROM User u join u.UserScores us where us.id.userId = :user_id";
            TypedQuery<Long> query3 = entityManager.createQuery(jpqll, Long.class);
            query3.setParameter("user_id", userARG.getId());
            List<Long> ids = query3.getResultList();
            for(User user : creatorUsers){
                jpql = "SELECT q FROM Quiz q where q.user.id = :user_id";
                TypedQuery<Quiz> query2 = entityManager.createQuery(jpql,Quiz.class);
                query2.setParameter("user_id", user.getId());
                creatorQuizzes = query2.getResultList();
                for (Quiz quiz : creatorQuizzes) {
                    for(Long id : ids) {
                        if(id == quiz.getId()){
                            creatorQuizzes.remove(quiz);
                            break;
                        }
                    }
                }
                user.setQuizzes(creatorQuizzes);
           }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error retrieving creatorUsers"));
        } finally {
            entityManager.close();
        }
    }

    public void selectQuizzes(List<Quiz> list){
        this.selectedQuizzes = list;
    }
















 //end of class
}
