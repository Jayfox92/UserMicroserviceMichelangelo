package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class StreamHistoryService implements StreamHistoryServiceInterface{

    private final StreamHistoryRepository streamHistoryRepository;
    private final MediaUserRepository mediaUserRepository;

    @Autowired
    public StreamHistoryService(StreamHistoryRepository streamHistoryRepository, MediaUserRepository mediaUserRepository) {
        this.streamHistoryRepository = streamHistoryRepository;
        this.mediaUserRepository = mediaUserRepository;
    }


    @Override
    public StreamHistory incrementStreamHistory(long userId, long mediaId) {
        Optional<StreamHistory> history = streamHistoryRepository.findByMediaUser_IdAndMediaId(userId,mediaId);
        if (history.isPresent()) {
            history.get().setStreamHistoryCount(history.get().getStreamHistoryCount() + 1);
            return streamHistoryRepository.save(history.get());
        }
        StreamHistory newHistory = new StreamHistory();
        MediaUser user = mediaUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("MediaUser","id",userId));
        newHistory.setStreamHistoryCount(1);
        newHistory.setMediaId((int)mediaId);
        newHistory.setMediaUser(user);
        streamHistoryRepository.save(newHistory);
        user.getStreamHistory().add(newHistory);
        mediaUserRepository.save(user);
        return newHistory;
    }
}
