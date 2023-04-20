package metier;


import com.DAO.Answer;
import com.DAO.Question;
import com.DAO.Quiz;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class QuestionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("quiz");
    private EntityManager em;

    public QuestionBean() {
    }
    private Question question;
    private Answer answer = new Answer();
    private Quiz quiz = new Quiz();

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    //methods
    public String GoToAddQuestion(Quiz quiz){
        this.quiz = quiz;
        this.question = new Question();
        return "/views/creatorROLES/addQuestion.xhtml?faces-redirect=true";
    }
    public String addQuestion() {
        em = entityManagerFactory.createEntityManager();
        question.setQuiz(quiz);
        quiz.getQuestions().add(question);
        try {
            em.getTransaction().begin();
            em.persist(question); // generates the question ID
            em.getTransaction().commit();
            for (Answer answer : question.getAnswers()) {
                answer.setQuestion(question);
                em.getTransaction().begin();
                em.persist(answer);
                em.getTransaction().commit();
            }
            question = new Question();
            answer = new Answer();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return "/views/creatorROLES/addQuestion.xhtml?faces-redirect=true";
    }

    public String addAnswer(){
        this.question.getAnswers().add(this.answer);
        this.answer = new Answer();
        return "/views/creatorROLES/addQuestion.xhtml?faces-redirect=true";
    }
    public String resetQuestion(){
        this.question = new Question();
        return "/views/creatorROLES/addQuestion.xhtml?faces-redirect=true";

    }


}
