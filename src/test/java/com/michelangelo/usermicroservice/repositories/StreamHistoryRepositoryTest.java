package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StreamHistoryRepositoryTest {

    @Autowired
    private StreamHistoryRepository streamHistoryRepository;

    // Dummy MediaUser instance for testing
    private MediaUser mediaUser;

    @BeforeEach
    public void setUp() {
        mediaUser = new MediaUser();
        mediaUser.setId(1L);
        // Set other fields as necessary

        // Save the MediaUser instance to the repository
        // mediaUserRepository.save(mediaUser); (Assuming you have a MediaUserRepository)
    }

    @Test
    public void testFindByMediaUser_IdAndMediaId() {
        // Given
        StreamHistory streamHistory = new StreamHistory();
        streamHistory.setId(1L);
        streamHistory.setMediaUser(mediaUser);
        streamHistory.setMediaId(1);
        // Set other fields as necessary

        // Save the StreamHistory instance to the repository
        streamHistoryRepository.save(streamHistory);

        // When
        Optional<StreamHistory> found = streamHistoryRepository.findByMediaUser_IdAndMediaId(1L, 1L);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
        assertThat(found.get().getMediaId()).isEqualTo(1L);
        assertThat(found.get().getMediaUser().getId()).isEqualTo(1L);
    }
}
