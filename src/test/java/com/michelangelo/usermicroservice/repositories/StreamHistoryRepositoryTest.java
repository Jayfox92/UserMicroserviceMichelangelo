package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class StreamHistoryRepositoryTest {

    @Autowired
    private StreamHistoryRepository streamHistoryRepository;

    @Autowired
    private MediaUserRepository mediaUserRepository;

    private MediaUser mediaUser;

    @BeforeEach
    public void setUp() {
        mediaUser = new MediaUser();
        mediaUser.setId(1L);
        mediaUserRepository.save(mediaUser);
    }


    // Test: Optional<StreamHistory> findByMediaUser_IdAndMediaId(Long userId, Long mediaId);
    @Test
    public void shouldReturnStreamHistoryForMediaUserByUserIdAndMediaId() {
        // Given
        StreamHistory streamHistory = new StreamHistory();
        streamHistory.setId(1L);
        streamHistory.setMediaUser(mediaUser);
        streamHistory.setMediaId(1);

        streamHistoryRepository.save(streamHistory);

        // When
        Optional<StreamHistory> found = streamHistoryRepository.findByMediaUser_IdAndMediaId(1L, 1L);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
        assertThat(found.get().getMediaId()).isEqualTo(1L);
        assertThat(found.get().getMediaUser().getId()).isEqualTo(1L);
    }

    @Test
    public void shouldReturnEmptyStreamHistoryForMediaUserByUserIdAndMediaId() {
        Long mediaId = 1L;
        StreamHistory streamHistory = new StreamHistory();
        streamHistoryRepository.save(streamHistory);

        Optional<StreamHistory> found = streamHistoryRepository.findByMediaUser_IdAndMediaId(mediaUser.getId(), mediaId);

        assertThat(found).isEmpty();
    }



    // Test: List<StreamHistory> findByMediaUser_Id(Long mediaUserId);
    @Test
    void shouldReturnStreamHistoryForMediaUserByUserId() {
        Long mediaId = 1L;
        Long streamId = 1L;
        StreamHistory streamHistory = new StreamHistory();
        streamHistory.setId(streamId);
        streamHistory.setMediaUser(mediaUser);
        streamHistory.setMediaId(mediaId);
        streamHistoryRepository.save(streamHistory);

        List<StreamHistory> found = streamHistoryRepository.findByMediaUser_Id(mediaUser.getId());

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(streamId);
        assertThat(found.get(0).getMediaId()).isEqualTo(mediaId);
    }

    @Test
    void shouldReturnEmptyStreamHistoryForMediaUserByUserId() {
        MediaUser mediaUserTwo = new MediaUser();
        StreamHistory streamHistory = new StreamHistory();
        streamHistoryRepository.save(streamHistory);

        List<StreamHistory> found = streamHistoryRepository.findByMediaUser_Id(mediaUserTwo.getId());

        assertThat(found).isEmpty();
    }
}
