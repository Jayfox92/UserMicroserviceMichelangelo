package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.ThumbsUpAndDownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ThumbsUpAndDownServiceTest {

    private ThumbsUpAndDown thumbsUpAndDown;
    private MediaUser mediaUser;
    private ThumbsUpAndDownService thumbsUpAndDownService;
    private ThumbsUpAndDownRepository thumbsUpAndDownRepositoryMock = mock(ThumbsUpAndDownRepository.class);
    private MediaUserRepository mediaUserRepositoryMock = mock(MediaUserRepository.class);


    @BeforeEach
    void setUp() {
        thumbsUpAndDownService = new ThumbsUpAndDownService(thumbsUpAndDownRepositoryMock, mediaUserRepositoryMock);
        mediaUser = new MediaUser();
        thumbsUpAndDown = new ThumbsUpAndDown();
        thumbsUpAndDown.setId(1L);
        thumbsUpAndDown.setMediaId(5);
        thumbsUpAndDown.setThumbsUp(true);
        thumbsUpAndDown.setThumbsDown(false);
        thumbsUpAndDown.setMediaUser(mediaUser);
        when(mediaUserRepositoryMock.findById(mediaUser.getId())).thenReturn(Optional.of(mediaUser));
    }

    // Tests for: placeThumbsUpAndDown()

    @Test
    void placeThumbsUpAndDownShouldThrowExceptionWithBothThumbsTrue() {
        thumbsUpAndDown.setThumbsUp(true);
        thumbsUpAndDown.setThumbsDown(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> thumbsUpAndDownService.placeThumbsUpAndDown(thumbsUpAndDown));
        assertEquals("Both thumbsUp and thumbsDown cannot both be true.", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenMediaUserDoesNotExist() {
        when(mediaUserRepositoryMock.findById(mediaUser.getId())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> thumbsUpAndDownService.placeThumbsUpAndDown(thumbsUpAndDown));
        assertEquals("MediaUser with 'id' was not found", exception.getMessage());
    }


    @Test
    void shouldUpdateExistingThumbsUpAndDown() {
        List<ThumbsUpAndDown> thumbsUpAndDownList = new ArrayList<>(List.of(new ThumbsUpAndDown(), new ThumbsUpAndDown(), thumbsUpAndDown, new ThumbsUpAndDown()));
        mediaUser.setThumbsUpAndDown(thumbsUpAndDownList);
        assertEquals(thumbsUpAndDown, thumbsUpAndDownService.placeThumbsUpAndDown(thumbsUpAndDown));
        verify(thumbsUpAndDownRepositoryMock).save(thumbsUpAndDown);
    }

    @Test
    void shouldCreateNewThumbsUpAndDown() {
        List<ThumbsUpAndDown> thumbsUpAndDownList = new ArrayList<>(List.of(new ThumbsUpAndDown(), new ThumbsUpAndDown(), new ThumbsUpAndDown()));
        mediaUser.setThumbsUpAndDown(thumbsUpAndDownList);
        assertEquals(thumbsUpAndDown, thumbsUpAndDownService.placeThumbsUpAndDown(thumbsUpAndDown));
    }


}