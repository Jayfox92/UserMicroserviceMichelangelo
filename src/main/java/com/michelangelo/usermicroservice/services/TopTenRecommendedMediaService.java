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

        // Hämta media som användaren inte har lyssnat på baserat på de tre mest spelade genrerna
        List<MediaVO> unlistenedMedia = findUnlistenedMediaByGenres(userId, genreIds);

        // Sortera och begränsa till de 10 mest relevanta medierna
        return unlistenedMedia.stream()
                .limit(10) // Begränsa till 10 media
                .collect(Collectors.toList());
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
                    genres.getLast().setCount(stream.getStreamHistoryCount());
                }
            }
        }
        genres.sort(Comparator.comparingInt(GenreVO::getCount));
        return genres;
    }

    public List<MediaVO> findUnlistenedMediaByGenres(Long userId, List<Integer> genreIds) {
        // Hämta användarens streamhistorik
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);

        // Hämta IDs för media som användaren har lyssnat på
        List<Integer> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .toList();

        List<MediaVO> unlistenedMedia = new ArrayList<>();

        // För varje genre-ID, hämta media från genre och filtrera
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
}
