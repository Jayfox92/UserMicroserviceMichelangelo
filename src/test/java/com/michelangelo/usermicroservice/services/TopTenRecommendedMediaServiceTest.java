package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopTenRecommendedMediaServiceTest {

    @Mock
    private StreamHistoryRepository streamHistoryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TopTenRecommendedMediaService topTenRecommendedMediaService;

    @Test
    public void shouldReturnUnlistenedMediaByGenres() {
        // Given
        Long userId = 1L;
        List<Integer> genreIds = Arrays.asList(1, 2);

        // Skapa testdata
        List<StreamHistory> streamHistories = Arrays.asList(
                new StreamHistory(1L, 1, 10),
                new StreamHistory(2L, 2, 5)
        );
        when(streamHistoryRepository.findByMediaUser_Id(userId)).thenReturn(streamHistories);

        GenreVO rockGenre = new GenreVO();
        rockGenre.setId(1L);
        rockGenre.setName("Rock");
        rockGenre.setCount(0);

        GenreVO popGenre = new GenreVO();
        popGenre.setId(2L);
        popGenre.setName("Pop");
        popGenre.setCount(0);

        MediaVO rockMedia = new MediaVO();
        rockMedia.setId(3L);
        rockMedia.setTitle("Rock Song");
        rockMedia.setReleaseDate(LocalDate.of(2023, 1, 1));
        rockMedia.setGenres(List.of(rockGenre));

        MediaVO popMedia = new MediaVO();
        popMedia.setId(4L);
        popMedia.setTitle("Pop Song");
        popMedia.setReleaseDate(LocalDate.of(2023, 1, 2));
        popMedia.setGenres(List.of(popGenre));

        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/1", MediaVO[].class))
                .thenReturn(new MediaVO[]{rockMedia});
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/2", MediaVO[].class))
                .thenReturn(new MediaVO[]{popMedia});

        // When
        List<MediaVO> result = topTenRecommendedMediaService.findUnlistenedMediaByGenres(userId, genreIds);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(media -> media.getId().equals(3L)));
        assertTrue(result.stream().anyMatch(media -> media.getId().equals(4L)));
    }
}
