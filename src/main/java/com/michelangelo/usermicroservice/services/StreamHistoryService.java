package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StreamHistoryService implements StreamHistoryServiceInterface{
    @Autowired
    private StreamHistoryRepository streamHistoryRepository;


    @Override
    public StreamHistory incrementStreamHistory(long userId, long mediaId) {
        StreamHistory history = streamHistoryRepository.findByMediaUser_IdAndMediaId(userId,mediaId)
                .orElseThrow(()-> new ResourceNotFoundException("StreamHistory", "mediaId",mediaId));
        history.setStreamHistoryCount(history.getStreamHistoryCount()+1);
        return streamHistoryRepository.save(history);
    }
}
