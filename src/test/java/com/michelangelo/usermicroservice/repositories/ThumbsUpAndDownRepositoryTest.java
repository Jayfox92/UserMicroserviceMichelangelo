package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ThumbsUpAndDownRepositoryTest {

    @Autowired
    private ThumbsUpAndDownRepository thumbsUpAndDownRepository;

    @Autowired
    private MediaUserRepository mediaUserRepository;

    private MediaUser mediaUserExists;
    private MediaUser invalidMediaUser;
    private boolean thumbsDown = true;
    private ThumbsUpAndDown thumbsUpAndDownOne;
    private ThumbsUpAndDown thumbsUpAndDownTwo;


    @BeforeEach
    void setUp() {
        mediaUserExists = new MediaUser();
        invalidMediaUser = new MediaUser();

        mediaUserRepository.save(mediaUserExists);
        mediaUserRepository.save(invalidMediaUser);

        thumbsUpAndDownOne = new ThumbsUpAndDown();
        thumbsUpAndDownTwo = new ThumbsUpAndDown();
        thumbsUpAndDownOne.setThumbsDown(thumbsDown);
        thumbsUpAndDownTwo.setThumbsDown(thumbsDown);
        thumbsUpAndDownOne.setMediaUser(mediaUserExists);
        thumbsUpAndDownTwo.setMediaUser(mediaUserExists);

        thumbsUpAndDownRepository.save(thumbsUpAndDownOne);
        thumbsUpAndDownRepository.save(thumbsUpAndDownTwo);
    }

    // Test: List<ThumbsUpAndDown> findAllByMediaUserAndThumbsDown(MediaUser mediaUser, boolean thumbsDown);
    @Test
    void shouldReturnAllMediaByUserAndThumbsdown() {
        List<ThumbsUpAndDown> returnedList = thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(mediaUserExists, thumbsDown);
        assertFalse(returnedList.isEmpty());
        assertEquals(2, returnedList.size());
        assertEquals(thumbsUpAndDownOne, returnedList.get(0));

    }

    @Test
    void shouldReturnEmptyMediaByUserAndThumbsdownWhenMediaUserNotExists() {
        List<ThumbsUpAndDown> returnedList = thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(invalidMediaUser, thumbsDown);
        assertTrue(returnedList.isEmpty());
    }
}