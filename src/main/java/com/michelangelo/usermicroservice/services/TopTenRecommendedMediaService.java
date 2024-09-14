package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopTenRecommendedMediaService implements TopTenRecommendedMediaServiceInterface {
    private final MediaUserRepository mediaUserRepository;
    private final StreamHistoryRepository streamHistoryRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public TopTenRecommendedMediaService(MediaUserRepository mediaUserRepository, StreamHistoryRepository streamHistoryRepository, RestTemplate restTemplate) {
        this.mediaUserRepository = mediaUserRepository;
        this.streamHistoryRepository = streamHistoryRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MediaVO> getTopTenRecommendedMedia(Long userId) {
        MediaUser mediaUser = mediaUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("MediaUser", "id", userId));
        List<GenreVO> topGenres = getTopThreeMostPlayedGenresFromMediaHistory(mediaUser.getStreamHistory());

        // Omvandla genre-IDs till Integer för att matcha metoden
        List<Integer> genreIds = topGenres.stream()
                .map(genreVO -> genreVO.getId().intValue()) // Konvertera Long till Integer
                .collect(Collectors.toList());

        List<MediaVO> unlistenedMedia = findUnlistenedMediaByGenres(userId, genreIds).stream()
                .limit(8)  // Begränsa till 8 media från topp 3 genrer
                .collect(Collectors.toList());

        // Hämta 2 media från andra genrer
        List<MediaVO> otherGenreMedia = findUnlistenedMediaFromOtherGenres(userId, genreIds).stream()
                .limit(2)  // Begränsa till 2 media från andra genrer
                .collect(Collectors.toList());

        // Slå samman resultaten och returnera topp 10
        unlistenedMedia.addAll(otherGenreMedia);
        return unlistenedMedia;
    }

    private List<GenreVO> getTopThreeMostPlayedGenresFromMediaHistory(List<StreamHistory> streamHistory) {
        List<GenreVO> genres = new ArrayList<>();
        boolean genreAdded;
        for (StreamHistory stream : streamHistory) {
            MediaVO mediaVO = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/" + stream.getMediaId(), MediaVO.class);
            for (GenreVO genreVO : mediaVO.getGenres()) {
                genreAdded = false;
                for (GenreVO genre : genres) {
                    if (genre.getId().equals(genreVO.getId())) {
                        genre.setCount(genre.getCount() + stream.getStreamHistoryCount());
                        genreAdded = true;
                    }
                }
                if (!genreAdded) {
                    genres.add(genreVO);
                    genres.get(genres.size() - 1).setCount(stream.getStreamHistoryCount());
                }
            }
        }
        genres.sort(Comparator.comparingInt(GenreVO::getCount).reversed());
        return genres.stream().limit(3).collect(Collectors.toList());  // Begränsa till topp 3 genrer
    }


    // Metod för att hitta media som användaren inte har lyssnat på baserat på genrer
    public List<MediaVO> findUnlistenedMediaByGenres(Long userId, List<Integer> genreIds) {
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);
        List<Integer> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .collect(Collectors.toList());

        List<MediaVO> unlistenedMedia = new ArrayList<>();
        for (Integer genreId : genreIds) {
            MediaVO[] mediaFromGenre = restTemplate.getForObject(
                    "http://MEDIAMICROSERVICE/media/genre/" + genreId, MediaVO[].class);

            if (mediaFromGenre != null) {
                for (MediaVO media : mediaFromGenre) {
                    if (!listenedMediaIds.contains(media.getId())) {
                        unlistenedMedia.add(media);
                    }
                }
            }
        }
        return unlistenedMedia;
    }
    // Metod för att hämta media från andra genrer (genrer som inte är bland användarens topp 3)
    public List<MediaVO> findUnlistenedMediaFromOtherGenres(Long userId, List<Integer> excludeGenreIds) {
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);
        List<Integer> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .collect(Collectors.toList());

        List<MediaVO> unlistenedMedia = new ArrayList<>();
        GenreVO[] allGenres = restTemplate.getForObject("http://MEDIAMICROSERVICE/genres", GenreVO[].class);

        if (allGenres != null) {
            List<GenreVO> otherGenres = Arrays.stream(allGenres)
                    .filter(genreVO -> !excludeGenreIds.contains(genreVO.getId().intValue()))
                    .collect(Collectors.toList());

            for (GenreVO genreVO : otherGenres) {
                MediaVO[] mediaFromGenre = restTemplate.getForObject(
                        "http://MEDIAMICROSERVICE/media/genre/" + genreVO.getId(), MediaVO[].class);

                if (mediaFromGenre != null) {
                    for (MediaVO media : mediaFromGenre) {
                        if (!listenedMediaIds.contains(media.getId())) {
                            unlistenedMedia.add(media);
                        }
                    }
                }
            }
        }
        return unlistenedMedia;
    }
}
