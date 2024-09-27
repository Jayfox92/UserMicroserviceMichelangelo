package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.VO.MediaWithStreamCountVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MediaUserServiceTest {

    private MediaUserService mediaUserService;
    private MediaUserRepository mediaUserRepositoryMock = mock(MediaUserRepository.class);
    private RestTemplate restTemplateMock = mock(RestTemplate.class);
    private MediaUser mediaUser;
    private final long userId = 1L;
    private final String userName = "user";

    @BeforeEach
    void setUp() {
        mediaUserService = new MediaUserService(mediaUserRepositoryMock,restTemplateMock);
        mediaUser = new MediaUser();
        mediaUser.setId(userId);
        mediaUser.setUserName(userName);
        ReflectionTestUtils.setField(mediaUserService,"maxLimit", 20);
    }

    // getMediaUser()
    @Test
    void shouldReturnMediaUserWhenIdExist() {
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.of(mediaUser));
        assertEquals(mediaUser, mediaUserService.getMediaUser(userId));
        verify(mediaUserRepositoryMock, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> mediaUserService.getMediaUser(userId));
        assertEquals("MediaUser with 'id' was not found", exception.getMessage());
        verify(mediaUserRepositoryMock, times(1)).findById(userId);
    }


    // getMediaUserByUserName()
    @Test
    void shouldReturnMediaUserWhenUsernameExist() {
        when(mediaUserRepositoryMock.findByUserName(userName)).thenReturn(Optional.of(mediaUser));
        assertEquals(mediaUser, mediaUserService.getMediaUserByUserName(userName));
        verify(mediaUserRepositoryMock, times(1)).findByUserName(userName);
    }

    @Test
    void shouldThrowExceptionWhenUsernameDoesNotExist() {
        when(mediaUserRepositoryMock.findByUserName(userName)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> mediaUserService.getMediaUserByUserName(userName));
        assertEquals("MediaUser with 'username' was not found", exception.getMessage());
        verify(mediaUserRepositoryMock, times(1)).findByUserName(userName);
    }


    // Test för getTopPlayedMedia
    @Test
    void shouldThrowExceptionWhenMaxLimitExceeded() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mediaUserService.getTopPlayedMedia(1, 100));
        assertEquals("Limit exceeds maximum allowed value of 20", exception.getMessage());
        verify(mediaUserRepositoryMock, never()).findById(userId);
    }

    @Test
    void shouldThrowExceptionIfMediaUserDoesNotExist() {
        when(mediaUserRepositoryMock.existsById(userId)).thenReturn(false);
        assertThrowsExactly(ResourceNotFoundException.class, () -> mediaUserService.getTopPlayedMedia(userId, 5), "User not found with id : " + userId);
        verify(mediaUserRepositoryMock, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyResultIfMediaUserExistsAndHasNoStreamHistory() {
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.of(mediaUser));
        mediaUser.setStreamHistory(new ArrayList<StreamHistory>());
        List<MediaWithStreamCountVO> result = mediaUserService.getTopPlayedMedia(userId, 5);
        verify(mediaUserRepositoryMock, times(1)).findById(userId);
        assertEquals(new ArrayList<MediaWithStreamCountVO>(), result);
    }

    @Test
    void shouldThrowExceptionWhenMediaMicroserviceFails() {
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.of(mediaUser));
        StreamHistory streamHistory = new StreamHistory();
        streamHistory.setMediaId(1);
        List<StreamHistory> streamHistoryList = new ArrayList<>(List.of(streamHistory));
        mediaUser.setStreamHistory(streamHistoryList);

        doThrow(RestClientException.class).when(restTemplateMock).getForObject(eq("http://MEDIAMICROSERVICE/media/media/" + streamHistoryList.get(0).getMediaId()), eq(MediaVO.class));

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> mediaUserService.getTopPlayedMedia(userId, 5));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
        verify(mediaUserRepositoryMock, times(1)).findById(userId);
        verify(restTemplateMock, times(1)).getForObject(eq("http://MEDIAMICROSERVICE/media/media/" + streamHistoryList.get(0).getMediaId()), eq(MediaVO.class));
    }

}
