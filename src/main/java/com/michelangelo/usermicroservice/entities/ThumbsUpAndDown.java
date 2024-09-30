package com.michelangelo.usermicroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class ThumbsUpAndDown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long mediaId;

    /*
     * up = false & down = false  -> Användaren har ej valt att gilla eller ogilla vald media
     * up = true  & down = false  -> Användaren har valt att gilla vald media med tumme upp
     * up = false & down = true   -> Användaren har valt att gilla vald media med tumme ner
     */
    @Column
    private boolean thumbsUp;
    @Column
    private boolean thumbsDown;

    @ManyToOne
    @JoinColumn(name = "media_user_id")
    @JsonIgnoreProperties({"thumbsUpAndDown", "streamHistory"})
    private MediaUser mediaUser;

    public ThumbsUpAndDown(){}

    public ThumbsUpAndDown(long id, MediaUser mediaUser, long mediaId, boolean thumbsUp, boolean thumbsDown){
        this.id = id;
        this.mediaUser = mediaUser;
        this.mediaId = mediaId;
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(boolean thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public boolean isThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(boolean thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public MediaUser getMediaUser() {
        return mediaUser;
    }

    public void setMediaUser(MediaUser mediaUser) {
        this.mediaUser = mediaUser;
    }
}
