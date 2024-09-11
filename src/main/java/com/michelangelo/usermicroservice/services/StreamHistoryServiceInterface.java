package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.StreamHistory;

public interface StreamHistoryServiceInterface {
    public StreamHistory incrementStreamHistory(long userId, long mediaId);

}
