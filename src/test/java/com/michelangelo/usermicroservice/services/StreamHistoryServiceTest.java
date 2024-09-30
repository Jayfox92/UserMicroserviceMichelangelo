package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class StreamHistoryServiceTest {

    private StreamHistoryService streamHistoryService;
    private StreamHistory streamHistory;
    private MediaUser mediaUser;
    private final long userId = 1L;
    private final long mediaId = 1L;

    private StreamHistoryRepository streamHistoryRepositoryMock = mock(StreamHistoryRepository.class);
    private MediaUserRepository mediaUserRepositoryMock = mock(MediaUserRepository.class);


    @BeforeEach
    void setUp() {
        streamHistoryService = new StreamHistoryService(streamHistoryRepositoryMock, mediaUserRepositoryMock);
        streamHistory = new StreamHistory();
        mediaUser = new MediaUser();

        streamHistory.setStreamHistoryCount(1);
        streamHistory.setMediaId((int)mediaId);
    }

    // incrementStreamHistory

    @Test
    void shouldUseExistingStreamHistoryToSaveStreamHistory() {
        when(streamHistoryRepositoryMock.findByMediaUser_IdAndMediaId(userId, mediaId)).thenReturn(Optional.of(streamHistory));
        when(streamHistoryRepositoryMock.save(streamHistory)).thenReturn(streamHistory);

        StreamHistory result = streamHistoryService.incrementStreamHistory(userId, mediaId);

        assertEquals(2, result.getStreamHistoryCount()); // att streamHistoryCount ökat med +1
        assertEquals(result, streamHistory); // att rätt streamHistory returneras
        verify(streamHistoryRepositoryMock, times(1)).save(streamHistory); // att sparandet anropats
    }

    @Test
    void shouldCreateNewStreamHistoryWhenNoneExistingStreamHistory() {
        mediaUser.setStreamHistory(new ArrayList<>());
        when(streamHistoryRepositoryMock.findByMediaUser_IdAndMediaId(userId, mediaId)).thenReturn(Optional.empty());
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.of(mediaUser));

        StreamHistory result = streamHistoryService.incrementStreamHistory(userId, mediaId);

        assertEquals(1, result.getStreamHistoryCount()); // att streamHistoryCount ökat med +1
        verify(mediaUserRepositoryMock, times(1)).save(mediaUser);
    }

    @Test
    void shouldThrowExceptionWhenMediaUserNotFound() {
        when(streamHistoryRepositoryMock.findByMediaUser_IdAndMediaId(userId, mediaId)).thenReturn(Optional.empty());
        when(mediaUserRepositoryMock.findById(userId)).thenThrow(new ResourceNotFoundException("MediaUser", "id", userId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> streamHistoryService.incrementStreamHistory(userId, mediaId));

        assertEquals("MediaUser with 'id' was not found", exception.getMessage());
    }
}