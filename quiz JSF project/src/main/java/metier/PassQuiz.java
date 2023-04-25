package metier;


import com.DAO.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class PassQuiz implements Serializable {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("quiz");
    private EntityManager entityManager;
    private Quiz passingQuiz;
    private Question currentQuestion;
    private List<Question> questions;
    private int currentQuestionIndex;
    private List<String> selectedAnswers;
    private List<String> currentAnswers;
    private Question lastQuestion;
    private float quizScore;
    private boolean isValidQuiz;
    public PassQuiz() {
        this.currentQuestionIndex = 0;
        this.quizScore = 0;
        isValidQuiz = true;

    }

    public Question getLastQuestion() {
        return lastQuestion;
    }

    public void setLastQuestion(Question lastQuestion) {
        this.lastQuestion = lastQuestion;
    }

    public float getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(float quizScore) {
        this.quizScore = quizScore;
    }

    public Quiz getPassingQuiz() {
        return passingQuiz;
    }

    public void setPassingQuiz(Quiz passingQuiz) {
        this.passingQuiz = passingQuiz;
    }

    public List<String> getCurrentAnswers() {
        return currentAnswers;
    }

    public void setCurrentAnswers(List<String> currentAnswers) {
        this.currentAnswers = currentAnswers;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<String> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(List<String> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public List<String> getselectedAnswers() {
        return selectedAnswers;
    }

    public void setselectedAnswers(List<String> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public void retrieve_quiz_Questions(){
        entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT q FROM Question q where q.quiz.id = :quiz_id";
            TypedQuery<Question> query = entityManager.createQuery(jpql, Question.class);
            query.setParameter("quiz_id",this.passingQuiz.getId());
            this.questions = new ArrayList<Question>();
            this.questions = query.getResultList();
            this.lastQuestion = this.questions.get(questions.size() - 1);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error retrieving questions"));
        } finally {
            entityManager.close();
        }
    }
    public void retrieve_question_answers(){
        entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT a FROM Answer a where a.question.id = :question_id";
            TypedQuery<Answer> query = entityManager.createQuery(jpql, Answer.class);
            query.setParameter("question_id",this.currentQuestion.getId());
            this.currentQuestion.setAnswers(query.getResultList());
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error retrieving answers"));
        } finally {
            entityManager.close();
        }
    }
public void reinitialise(){
    passingQuiz = new Quiz();
    currentQuestion = new Question();
    questions = new ArrayList<Question>();
    currentQuestionIndex = 0;
    selectedAnswers = new ArrayList<String>();
    currentAnswers = new ArrayList<String>();
    lastQuestion = new Question();
    isValidQuiz = true;
    quizScore = 0;
}


    public String selectPassingQuiz(Quiz qze){
        reinitialise();
        this.passingQuiz = qze;
        //retrieve the questions of this passing quiz from the database.
        retrieve_quiz_Questions();
        if (this.questions != null && !this.questions.isEmpty()){
            this.currentQuestion = this.questions.get(0);
          if(this.currentQuestion != null) {
              //retrieve the current answers of the current question from the database.
              retrieve_question_answers();
              if (this.currentQuestion.getAnswers() != null && !this.currentQuestion.getAnswers().isEmpty()) {
                  this.currentAnswers = new ArrayList<String>();
                  for (Answer answer : this.currentQuestion.getAnswers()) {
                      this.currentAnswers.add(answer.getStatement());
                  }
              }
          }
        }
        return "/views/learnerROLES/passQuiz?faces-redirect=true";
    }
    public void CalculateScore(){
        if(!selectedAnswers.isEmpty()){
            List<String> LocalSelectedAnswers = selectedAnswers;
            this.selectedAnswers = new ArrayList<String>();
            List<String> correctedAnswers = new ArrayList<String>();
            for(Answer answer : this.currentQuestion.getAnswers()){
                if(answer.getCorrect().equals("correct")){
                    correctedAnswers.add(answer.getStatement());
                }
            }
            boolean right = true;
            for(String answerStm : LocalSelectedAnswers){
                if (!correctedAnswers.contains(answerStm)) {
                    right = false;
                    break;
                }
            }
            if(right)
                for(String correctAnswerStm : correctedAnswers){
                    if (!LocalSelectedAnswers.contains(correctAnswerStm)) {
                        right = false;
                        break;
                    }
                }
            if(right) quizScore += this.currentQuestion.getGrade();
        }
    }

    public String nextQuestion(){
        CalculateScore();
        this.currentQuestionIndex++;
        this.currentQuestion = this.questions.get(this.currentQuestionIndex);
        //retrieve the current answers of the current question from the database.
        retrieve_question_answers();
        this.currentAnswers = new ArrayList<String>();
        if(!this.currentQuestion.getAnswers().isEmpty()){
            for (Answer answer : this.currentQuestion.getAnswers()) {
                this.currentAnswers.add(answer.getStatement());
            }

        }
        return "/views/learnerROLES/passQuiz?faces-redirect=true";
    }

    public String submitQuiz(User user){
        CalculateScore();
        UserQuizScoreId id = new UserQuizScoreId(user.getId(), passingQuiz.getId());
        UserQuizScore userQuizScore = new UserQuizScore();
        userQuizScore.setId(id);
        userQuizScore.setObtainedScore(quizScore);
        user.getUserScores().add(userQuizScore);
        passingQuiz.getQuizScores().add(userQuizScore);
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(userQuizScore);
            transaction.commit();
            //retrieve the user's quizzes after adding a new quiz to the database.
            //retrieveQuizzes(user);
            return "/views/learnerROLES/marks.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error adding user_quiz_score !"));
            return null;
        } finally {
            entityManager.close();
            reinitialise();
            isValidQuiz = false;
        }
    }



    public void isItValid() {
        if (!isValidQuiz) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../learnerIndex.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }












    //end of class
}
