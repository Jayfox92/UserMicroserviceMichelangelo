package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
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
import java.util.Arrays;
import java.util.Collections;
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
    private TopTenRecommendedMediaService topTenRecommendedMediaService = new TopTenRecommendedMediaService(mediaUserRepository,streamHistoryRepository,thumbsUpAndDownRepository,restTemplate);
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

    @Test
    void shouldReturnListOfMediaForGivenMediaType() {
        String mediaType = "Video";
        MediaVO[] mockMediaArray = new MediaVO[]{
                new MediaVO(1L, "Film 1"),
                new MediaVO(2L, "Film 2")
        };
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/getallbymediatype/" + mediaType), eq(MediaVO[].class))).thenReturn(mockMediaArray);

        List<MediaVO> result = topTenRecommendedMediaService.getListOfAllMedia(mediaType);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Film 1", result.get(0).getTitle());
        assertEquals("Film 2", result.get(1).getTitle());

        verify(restTemplate, times(1))
                .getForObject(eq("http://MEDIAMICROSERVICE/media/media/getallbymediatype/" + mediaType),
                        eq(MediaVO[].class));
    }

    @Test
    void shouldReturnTopThreeMostPlayedGenresFromMediaHistory() {
        List<StreamHistory> streamHistories = Arrays.asList(
                new StreamHistory(1L, 1, 5), // mediaId 1, streamHistoryCount 5
                new StreamHistory(2L, 2, 3), // mediaId 2, streamHistoryCount 3
                new StreamHistory(3L, 3, 2)  // mediaId 3, streamHistoryCount 2
        );

        MediaVO media1 = new MediaVO(1L, "Media 1", Arrays.asList(new GenreVO(1L, "Genre1", 0), new GenreVO(2L, "Genre2", 0)));
        MediaVO media2 = new MediaVO(2L, "Media 2", Arrays.asList(new GenreVO(1L, "Genre1", 0), new GenreVO(3L, "Genre3", 0)));
        MediaVO media3 = new MediaVO(3L, "Media 3", Arrays.asList(new GenreVO(2L, "Genre2", 0)));

        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/1"), eq(MediaVO.class))).thenReturn(media1);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/2"), eq(MediaVO.class))).thenReturn(media2);
        when(restTemplate.getForObject(eq("http://MEDIAMICROSERVICE/media/media/3"), eq(MediaVO.class))).thenReturn(media3);

        List<Long> result = topTenRecommendedMediaService.getTopThreeMostPlayedGenresFromMediaHistory(streamHistories);

        assertEquals(3, result.size());
        assertEquals(1L, result.get(0));
        assertEquals(2L, result.get(1));
        assertEquals(3L, result.get(2));

        verify(restTemplate, times(3)).getForObject(anyString(), eq(MediaVO.class));
    }

    @Test
    void shouldReturnEmptyListIfStreamHistoryIsEmpty() {
        List<StreamHistory> streamHistories = new ArrayList<>();

        List<Long> result = topTenRecommendedMediaService.getTopThreeMostPlayedGenresFromMediaHistory(streamHistories);

        assertEquals(0, result.size());

        verify(restTemplate, never()).getForObject(anyString(), eq(MediaVO.class));
    }

    @Test
    void shouldRemoveMediaWithThumbsDown() {
        MediaUser mediaUser = new MediaUser(1L, "testuser");  // Example MediaUser
        List<MediaVO> mediaList = Arrays.asList(
                new MediaVO(1L, "Media1"),
                new MediaVO(2L, "Media2"),
                new MediaVO(3L, "Media3")
        );

        List<ThumbsUpAndDown> thumbsDownList = Arrays.asList(
                new ThumbsUpAndDown(1L, mediaUser, 1L, false, true),  // Thumbs down for mediaId 1
                new ThumbsUpAndDown(2L, mediaUser, 3L, false, true)   // Thumbs down for mediaId 3
        );
        when(thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(mediaUser, true)).thenReturn(thumbsDownList);

        List<MediaVO> result = topTenRecommendedMediaService.removeMediaWithThumbDown(mediaUser, mediaList);

        assertEquals(1, result.size());  // Only media with ID 2 should remain
        assertEquals(2L, result.get(0).getId());

        verify(thumbsUpAndDownRepository, times(1)).findAllByMediaUserAndThumbsDown(mediaUser, true);
    }

    @Test
    void shouldReturnAllMediaIfNoThumbsDown() {
        MediaUser mediaUser = new MediaUser(1L, "testuser");
        List<MediaVO> mediaList = Arrays.asList(
                new MediaVO(1L, "Media1"),
                new MediaVO(2L, "Media2"),
                new MediaVO(3L, "Media3")
        );

        when(thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(mediaUser, true)).thenReturn(Collections.emptyList());

        List<MediaVO> result = topTenRecommendedMediaService.removeMediaWithThumbDown(mediaUser, mediaList);

        assertEquals(3, result.size());  // All media should remain
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(3L, result.get(2).getId());

        verify(thumbsUpAndDownRepository, times(1)).findAllByMediaUserAndThumbsDown(mediaUser, true);
    }

    @Test
    void shouldReturnEmptyListIfAllMediaHasThumbsDown() {
        MediaUser mediaUser = new MediaUser(1L, "testuser");
        List<MediaVO> mediaList = Arrays.asList(
                new MediaVO(1L, "Media1"),
                new MediaVO(2L, "Media2"),
                new MediaVO(3L, "Media3")
        );

        List<ThumbsUpAndDown> thumbsDownList = Arrays.asList(
                new ThumbsUpAndDown(1L, mediaUser, 1L, false, true),
                new ThumbsUpAndDown(2L, mediaUser, 2L, false, true),
                new ThumbsUpAndDown(3L, mediaUser, 3L, false, true)
        );
        when(thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(mediaUser, true)).thenReturn(thumbsDownList);

        List<MediaVO> result = topTenRecommendedMediaService.removeMediaWithThumbDown(mediaUser, mediaList);

        assertEquals(0, result.size());  // No media should remain since all have thumbs down

        verify(thumbsUpAndDownRepository, times(1)).findAllByMediaUserAndThumbsDown(mediaUser, true);
    }



}