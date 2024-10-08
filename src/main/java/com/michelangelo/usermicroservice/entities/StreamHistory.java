package com.michelangelo.usermicroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class StreamHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long mediaId;
    @Column
    private int streamHistoryCount;

    @ManyToOne
    @JoinColumn(name = "media_user_id")
    @JsonIgnoreProperties(value = "streamHistory")
    private MediaUser mediaUser;


    public StreamHistory(){}

    public StreamHistory(long id, int mediaId, int streamHistoryCount) {
        this.id = id;
        this.mediaId = mediaId;
        this.streamHistoryCount = streamHistoryCount;
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

    public int getStreamHistoryCount() {
        return streamHistoryCount;
    }

    public void setStreamHistoryCount(int streamHistoryCount) {
        this.streamHistoryCount = streamHistoryCount;
    }

    public MediaUser getMediaUser() {
        return mediaUser;
    }

    public void setMediaUser(MediaUser mediaUser) {
        this.mediaUser = mediaUser;
    }
}
