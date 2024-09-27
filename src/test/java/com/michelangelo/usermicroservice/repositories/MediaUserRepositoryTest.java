package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class MediaUserRepositoryTest {

    @Autowired
    private MediaUserRepository mediaUserRepository;

    private MediaUser mediaUser;
    private String usernameThatExists = "username";
    private String usernameThatDontExists = "test username";

    @BeforeEach
    void setUp() {
        mediaUser = new MediaUser();
        mediaUser.setUserName(usernameThatExists);
        mediaUserRepository.save(mediaUser);
    }

    @Test
    void shouldReturnMediaUserByUsername() {
        Optional<MediaUser> resultMediaUser = mediaUserRepository.findByUserName(usernameThatExists);
        assertThat(resultMediaUser).isPresent();
        assertEquals(usernameThatExists, resultMediaUser.get().getUserName());
    }

    @Test
    void shouldNotReturnMediaUser() {
        Optional<MediaUser> resultMediaUser = mediaUserRepository.findByUserName(usernameThatDontExists);
        assertThat(resultMediaUser).isNotPresent();
    }
}