package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.VO.MediaWithStreamCountVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

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

    @Test
    void shouldThrowExceptionIfMediaUserDoesNotExist(){
        when(mediaUserRepositoryMock.existsById(userId)).thenReturn(false);
        assertThrowsExactly(ResourceNotFoundException.class, () -> mediaUserService.getTopPlayedMedia(userId),"User not found with id : "+userId);
        verify(mediaUserRepositoryMock, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyResultIfMediaUserExistsAndHasNoStreamHistory(){
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.of(mediaUser));
        mediaUser.setStreamHistory(new ArrayList<StreamHistory>());
        List<MediaWithStreamCountVO> result = mediaUserService.getTopPlayedMedia(userId);
        verify(mediaUserRepositoryMock,times(1)).findById(userId);
        assertEquals(new ArrayList<MediaWithStreamCountVO>(), result);

    }

    @Test
    void shouldReturnTopPlayedMediaWithStreamCount(){
        when(mediaUserRepositoryMock.findById(userId)).thenReturn(Optional.of(mediaUser));
        StreamHistory history1 = new StreamHistory(1,1,1);
        StreamHistory history2 = new StreamHistory(2,2,2);
        StreamHistory history3 = new StreamHistory(3,3,3);

        List<StreamHistory> userHistory = Arrays.asList(history1,history2,history3);
        mediaUser.setStreamHistory(userHistory);

        MediaVO media1 = new MediaVO(1L, "Media1");
        MediaVO media2 = new MediaVO(2L, "Media2");
        MediaVO media3 = new MediaVO(3L, "Media3");
        when(restTemplateMock.getForObject(eq("http://MEDIAMICROSERVICE/media/media/1"), eq(MediaVO.class)))
                .thenReturn(media1);
        when(restTemplateMock.getForObject(eq("http://MEDIAMICROSERVICE/media/media/2"), eq(MediaVO.class)))
                .thenReturn(media2);
        when(restTemplateMock.getForObject(eq("http://MEDIAMICROSERVICE/media/media/3"), eq(MediaVO.class)))
                .thenReturn(media3);

        List<MediaWithStreamCountVO> result = mediaUserService.getTopPlayedMedia(userId);

        assertEquals(3, result.size());
        assertEquals(3, result.get(0).getStreamHistoryCount());
        assertEquals(2, result.get(1).getStreamHistoryCount());
        assertEquals(1, result.get(2).getStreamHistoryCount());

        verify(mediaUserRepositoryMock, times(1)).findById(userId);
        verify(restTemplateMock, times(3)).getForObject(anyString(), eq(MediaVO.class));
    }

}