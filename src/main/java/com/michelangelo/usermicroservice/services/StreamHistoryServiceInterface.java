package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.StreamHistory;

public interface StreamHistoryServiceInterface {
     StreamHistory incrementStreamHistory(long userId, long mediaId);

}
