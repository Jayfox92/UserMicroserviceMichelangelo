package com.michelangelo.usermicroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class MediaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(length = 35)
    private String userName;
    @Column(length = 50)
    private String email;
    @OneToMany(mappedBy = "mediaUser")
    @JsonIgnoreProperties(value = "mediaUser")
    private List<StreamHistory> streamHistory;
    @OneToMany(mappedBy = "mediaUser", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "mediaUser")
    private List<ThumbsUpAndDown> thumbsUpAndDown;

    public MediaUser(long id, String userName){
        this.id = id;
        this.userName = userName;
    }

    public MediaUser(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<StreamHistory> getStreamHistory() {
        return streamHistory;
    }

    public void setStreamHistory(List<StreamHistory> streamHistory) {
        this.streamHistory = streamHistory;
    }

    public List<ThumbsUpAndDown> getThumbsUpAndDown() {
        return thumbsUpAndDown;
    }

    public void setThumbsUpAndDown(List<ThumbsUpAndDown> thumbsUpAndDown) {
        this.thumbsUpAndDown = thumbsUpAndDown;
    }
}
