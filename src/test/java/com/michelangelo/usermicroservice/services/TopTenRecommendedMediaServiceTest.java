package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import com.michelangelo.usermicroservice.repositories.ThumbsUpAndDownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TopTenRecommendedMediaServiceTest {
    MediaUserRepository mediaUserRepository = mock(MediaUserRepository.class);
    StreamHistoryRepository streamHistoryRepository = mock(StreamHistoryRepository.class);
    RestTemplate restTemplate = mock(RestTemplate.class);
    ThumbsUpAndDownRepository thumbsUpAndDownRepository = mock(ThumbsUpAndDownRepository.class);
    List<MediaVO> medias;
    @BeforeEach
    void setUp() {
        medias = new ArrayList<>();
        List<GenreVO> genres = new ArrayList<>();
        genres.add(new GenreVO(1L, "Rock", 0));
        MediaVO media = new MediaVO(1L, "The Good", null, genres);
        medias.add(media);
        genres.add(new GenreVO(2L, "Pop", 0));
        media = new MediaVO(2L, "The bad", null, genres);
        medias.add(media);
        genres.add(new GenreVO(3L, "Jazz", 0));
        media = new MediaVO(3L, "The ugly", null, genres);
        medias.add(media);
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/1", MediaVO.class)).thenReturn(medias.get(0));
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/2", MediaVO.class)).thenReturn(medias.get(1));
        when(restTemplate.getForObject("http://MEDIAMICROSERVICE/media/3", MediaVO.class)).thenReturn(medias.get(2));
    }

    @Test
    void getTopThreeMostPlayedGenresFromMediaHistoryShouldReturnEmptyListWhenGivenEmptyList() {
/*
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, restTemplate);
        List<StreamHistory> streamHistories = new ArrayList<>();
        assertTrue(topTenRecommendedMediaService.getTopThreeMostPlayedGenresFromMediaHistory(streamHistories).isEmpty());
*/
    }

    @Test
    void getTopThreeMostPlayedGenresFromMediaHistoryShouldReturnListWithThreeItemsInSequenceRockPopJazz() {
/*
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, restTemplate);
        List<StreamHistory> streamHistories = new ArrayList<>();
        StreamHistory streamHistory = new StreamHistory();
        streamHistory.setId(1);
        streamHistory.setMediaId(1);
        streamHistory.setStreamHistoryCount(1);
        streamHistory.setMediaUser(null);
        streamHistories.add(streamHistory);
        streamHistory = new StreamHistory();
        streamHistory.setId(2);
        streamHistory.setMediaId(2);
        streamHistory.setStreamHistoryCount(1);
        streamHistory.setMediaUser(null);
        streamHistories.add(streamHistory);
        streamHistory = new StreamHistory();
        streamHistory.setId(3);
        streamHistory.setMediaId(3);
        streamHistory.setStreamHistoryCount(1);
        streamHistory.setMediaUser(null);
        streamHistories.add(streamHistory);
        List<GenreVO> result = topTenRecommendedMediaService.getTopThreeMostPlayedGenresFromMediaHistory(streamHistories);
        assertThat(result.get(0).getName()).isEqualTo("Rock");
        assertThat(result.get(1).getName()).isEqualTo("Pop");
        assertThat(result.get(2).getName()).isEqualTo("Jazz");
*/
    }


    // Catch delarna i metoderna

    // Try Catch tester för alla metoder
    @Test
    void shouldThrowRestClientExceptionWhenTryingToGetMediaWithId(){
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, thumbsUpAndDownRepository, restTemplate);

        int mediaId = 10;
        StreamHistory streamHistory = new StreamHistory();
        streamHistory.setMediaId(mediaId);
        List<StreamHistory> streamHistoriyList = new ArrayList<>(List.of(streamHistory));

        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/" + mediaId), eq(MediaVO.class))).thenThrow(RestClientException.class);
        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> topTenRecommendedMediaService.getTopThreeMostPlayedGenresFromMediaHistory(streamHistoriyList));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }

    @Test
    void shouldThrowRestClientExceptionWhenTryingToGetMediaByGenreIdAndMediaType(){
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, thumbsUpAndDownRepository, restTemplate);
        long userId = 1L;
        String mediaType = "Musik";
        List<StreamHistory> streamHistoryList = new ArrayList<>();
        List<Long> ids = new ArrayList<>(List.of(1L, 2L, 3L));

        when(streamHistoryRepository.findByMediaUser_Id(userId)).thenReturn(streamHistoryList);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/genre/" + ids.getFirst() + "/" + mediaType), eq(MediaVO[].class))).thenThrow(RestClientException.class);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> topTenRecommendedMediaService.findUnlistenedMediaFromTopGenres(userId, ids, mediaType));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }

    @Test
    void shouldThrowRestClientExceptionWhenTryingToGetAllGenres(){
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, thumbsUpAndDownRepository, restTemplate);
        long userId = 1L;
        String mediaType = "Musik";
        List<Long> ids = new ArrayList<>(List.of(1L, 2L, 3L));
        List<StreamHistory> streamHistoryList = new ArrayList<>();

        when(streamHistoryRepository.findByMediaUser_Id(userId)).thenReturn(streamHistoryList);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/genre/getall"), eq(GenreVO[].class))).thenThrow(RestClientException.class);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> topTenRecommendedMediaService.findUnlistenedMediaFromOtherGenres(userId, ids, mediaType));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }

    @Test
    void shouldThrowRestClientExceptionWhenTryingToGetMediaByGenreIdAndMediaTypeAfterGetAllGenre(){
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, thumbsUpAndDownRepository, restTemplate);
        long userId = 1L;
        String mediaType = "Musik";
        List<Long> ids = new ArrayList<>(List.of(10L, 20L, 30L));
        List<StreamHistory> streamHistoryList = new ArrayList<>();
        GenreVO genre = new GenreVO(1L, "Pop", 0);
        GenreVO[] genreList = new GenreVO[]{genre};

        when(streamHistoryRepository.findByMediaUser_Id(userId)).thenReturn(streamHistoryList);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/genre/getall"), eq(GenreVO[].class))).thenReturn(genreList);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/genre/" + genre.getId() + "/" + mediaType), eq(MediaVO[].class))).thenThrow(RestClientException.class);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> topTenRecommendedMediaService.findUnlistenedMediaFromOtherGenres(userId, ids, mediaType));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }


    @Test
    void shouldThrowRestClientExceptionWhenTryingToGetAllMediaByMediaType(){
        String mediaType = "Musik";
        TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository, streamHistoryRepository, thumbsUpAndDownRepository, restTemplate);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/getallbymediatype/" + mediaType), eq(MediaVO[].class))).thenThrow(RestClientException.class);

        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> topTenRecommendedMediaService.getListOfAllMedia(mediaType));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }





}