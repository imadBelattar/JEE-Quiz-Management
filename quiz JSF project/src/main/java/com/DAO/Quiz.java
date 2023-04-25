package com.DAO;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "theme", nullable = false)
    private String theme;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "created", nullable = false)
    private String created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;
    @OneToMany(mappedBy = "id.quizId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserQuizScore> QuizScores = new ArrayList<UserQuizScore>();

    public Quiz() {
        questions = new ArrayList<Question>();

    }

    public List<UserQuizScore> getQuizScores() {
        return QuizScores;
    }

    public void setQuizScores(List<UserQuizScore> quizScores) {
        QuizScores = quizScores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
