package com.DAO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;



@Embeddable
public class UserQuizScoreId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quiz_id")
    private Long quizId;

    public UserQuizScoreId() {
    }

    public UserQuizScoreId(Long userId, Long quizId) {
        this.userId = userId;
        this.quizId = quizId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
}