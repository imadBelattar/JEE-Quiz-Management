package com.DAO;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "userquizscore")
public class UserQuizScore implements Serializable {
    @EmbeddedId
    private UserQuizScoreId id;
    @Column(name = "obtained_score")
    private float obtainedScore;

    public UserQuizScore() {
        id = new UserQuizScoreId();
    }

    public UserQuizScoreId getId() {
        return id;
    }

    public void setId(UserQuizScoreId id) {
        this.id = id;
    }
    public float getObtainedScore() {
        return obtainedScore;
    }

    public void setObtainedScore(float obtainedScore) {
        this.obtainedScore = obtainedScore;
    }
}
