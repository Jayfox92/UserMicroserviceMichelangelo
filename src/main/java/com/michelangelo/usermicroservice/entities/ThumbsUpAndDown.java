package com.michelangelo.usermicroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class ThumbsUpAndDown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long mediaId;
    @Column
    private boolean thumbsUp;
    //false innebär att användaren ej gjort ett val (tumme upp eller tumme ner på ett media)
    //true innebär att användaren antingen valt att gilla (thumbsUp) eller ogilla (thumbsDown) ett media
    //är båda false har användaren ej valt att gilla eller ogilla ett media
    //todo båda nedan får ej vara true (anvädaren kan ej både gilla och ogilla samma media) kontroll för detta behöver implementeras
    //todo när man ändrar nedan värden
    @Column
    private boolean thumbsDown;

    // private double likeOrDislike = 0; // -1 equals dislike | 1 equals like
    // private String like = "upp"; // ner
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
