package com.michelangelo.usermicroservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class StreamHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private int mediaId;
    @Column
    private int streamHistoryCount;
    @ManyToOne
    @JoinColumn(name = "Media_User_stream_history")
    @JsonIgnoreProperties(value = "streamHistory")
    private MediaUser mediaUser;

    public StreamHistory(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
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
