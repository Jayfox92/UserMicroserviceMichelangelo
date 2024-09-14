package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopTenRecommendedMediaServiceTest {

    @Mock
    private MediaUserRepository mediaUserRepository;
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

        // Skapa testdata för StreamHistory
        List<StreamHistory> streamHistories = Arrays.asList(
                new StreamHistory(1L, 1, 10), // Lyssnat på media med ID 1
                new StreamHistory(2L, 2, 5)   // Lyssnat på media med ID 2
        );
        when(streamHistoryRepository.findByMediaUser_Id(userId)).thenReturn(streamHistories);

        // Skapa genrer
        GenreVO rockGenre = new GenreVO();
        rockGenre.setId(1L);
        rockGenre.setName("Rock");

        GenreVO popGenre = new GenreVO();
        popGenre.setId(2L);
        popGenre.setName("Pop");

        // Skapa media med olika genrer
        MediaVO rockMedia = new MediaVO();
        rockMedia.setId(3L);  // Nytt media som användaren inte har lyssnat på
        rockMedia.setTitle("Rock Song");
        rockMedia.setGenres(List.of(rockGenre));

        MediaVO popMedia = new MediaVO();
        popMedia.setId(4L);  // Nytt media som användaren inte har lyssnat på
        popMedia.setTitle("Pop Song");
        popMedia.setGenres(List.of(popGenre));

        // Simulera API-anrop för att hämta media baserat på genrer
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/1", MediaVO[].class))
                .thenReturn(new MediaVO[]{rockMedia});
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/2", MediaVO[].class))
                .thenReturn(new MediaVO[]{popMedia});

        // When
        List<MediaVO> result = topTenRecommendedMediaService.findUnlistenedMediaByGenres(userId, genreIds);

        // Then
        assertEquals(2, result.size()); // Endast två media eftersom de är olyssnade
        assertTrue(result.stream().anyMatch(media -> media.getId().equals(3L))); // Rock Song
        assertTrue(result.stream().anyMatch(media -> media.getId().equals(4L))); // Pop Song
        assertFalse(result.stream().anyMatch(media -> media.getId().equals(1L))); // Media användaren redan lyssnat på ska inte vara med
        assertFalse(result.stream().anyMatch(media -> media.getId().equals(2L))); // Media användaren redan lyssnat på ska inte vara med
    }

    @Test
    public void shouldReturnUnlistenedMediaFromOtherGenres() {
        // Given
        Long userId = 1L;
        List<Integer> excludeGenreIds = Arrays.asList(1, 2); // Exkludera genre IDs 1 och 2

        // Mocka StreamHistory för användaren
        List<StreamHistory> userStreamHistory = Arrays.asList(
                new StreamHistory(1L, 1, 10), // Lyssnat på media med ID 1
                new StreamHistory(2L, 2, 5)   // Lyssnat på media med ID 2
        );
        when(streamHistoryRepository.findByMediaUser_Id(userId)).thenReturn(userStreamHistory);

        // Mocka alla genrer från Media-mikrotjänsten
        GenreVO rockGenre = new GenreVO();
        rockGenre.setId(1L);
        rockGenre.setName("Rock");

        GenreVO popGenre = new GenreVO();
        popGenre.setId(2L);
        popGenre.setName("Pop");

        GenreVO classicalGenre = new GenreVO();
        classicalGenre.setId(3L);
        classicalGenre.setName("Classical");

        GenreVO hiphopGenre = new GenreVO();
        hiphopGenre.setId(4L);
        hiphopGenre.setName("Hip Hop");

        GenreVO[] allGenres = new GenreVO[]{rockGenre, popGenre, classicalGenre, hiphopGenre};
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/genres", GenreVO[].class)).thenReturn(allGenres);

        // Mocka media från genrer som inte är exkluderade (Classical och Hip Hop)
        MediaVO classicalMedia = new MediaVO();
        classicalMedia.setId(3L);
        classicalMedia.setTitle("Classical Song");

        MediaVO hiphopMedia = new MediaVO();
        hiphopMedia.setId(4L);
        hiphopMedia.setTitle("Hip Hop Song");

        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/3", MediaVO[].class))
                .thenReturn(new MediaVO[]{classicalMedia});
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/4", MediaVO[].class))
                .thenReturn(new MediaVO[]{hiphopMedia});

        // When
        List<MediaVO> result = topTenRecommendedMediaService.findUnlistenedMediaFromOtherGenres(userId, excludeGenreIds);

        // Then
        assertEquals(2, result.size()); // Det bör finnas två media från andra genrer
        assertTrue(result.contains(classicalMedia)); // Verifiera att "Classical Song" är med i resultatet
        assertTrue(result.contains(hiphopMedia)); // Verifiera att "Hip Hop Song" är med i resultatet
    }
}
