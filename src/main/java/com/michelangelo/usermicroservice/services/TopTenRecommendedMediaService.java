package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.StreamHistoryRepository;
import com.michelangelo.usermicroservice.repositories.ThumbsUpAndDownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;

@Service
public class TopTenRecommendedMediaService implements TopTenRecommendedMediaServiceInterface {
    private final MediaUserRepository mediaUserRepository;
    private final StreamHistoryRepository streamHistoryRepository;
    private final ThumbsUpAndDownRepository thumbsUpAndDownRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public TopTenRecommendedMediaService(MediaUserRepository mediaUserRepository,
                                         StreamHistoryRepository streamHistoryRepository,
                                         ThumbsUpAndDownRepository thumbsUpAndDownRepository,
                                         RestTemplate restTemplate) {
        this.mediaUserRepository = mediaUserRepository;
        this.streamHistoryRepository = streamHistoryRepository;
        this.thumbsUpAndDownRepository = thumbsUpAndDownRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MediaVO> getTopTenRecommendedMedia(Long userId) {
        MediaUser mediaUser = mediaUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("MediaUser", "id", userId));
        List<Long> topGenres = getTopThreeMostPlayedGenresFromMediaHistory(mediaUser.getStreamHistory());

        // Hämta all media från top genrer
        List<MediaVO> unlistenedMediaFromTopGenres = findUnlistenedMediaFromTopGenres(userId, topGenres);

        // Hämta all media från andra genrer
        List<MediaVO> unlistenedMediaFromOtherGenres = findUnlistenedMediaFromOtherGenres(userId, topGenres);

        // Ta bort all hittat media med tumma ner
        unlistenedMediaFromTopGenres = removeMediaWithThumbDown(mediaUser, unlistenedMediaFromTopGenres);
        unlistenedMediaFromOtherGenres = removeMediaWithThumbDown(mediaUser, unlistenedMediaFromOtherGenres);

        // Välj random 8 + 2 media.
        // Om där inte är hittat 8 + 2 media välj random resterande från en lista av media utan tumma ner
        List<MediaVO> topTenRecommendedMedia = new ArrayList<>();
        int numberOfMediasToFind = 0;

        if (unlistenedMediaFromTopGenres.size() <= 8) {
            numberOfMediasToFind = 8 - unlistenedMediaFromTopGenres.size();
            topTenRecommendedMedia.addAll(unlistenedMediaFromTopGenres);
        }
        else{
            Random random = new Random();
            int index = 0;
            while(topTenRecommendedMedia.size() < 8){
                index = random.nextInt(unlistenedMediaFromTopGenres.size());
                if(!topTenRecommendedMedia.contains(unlistenedMediaFromTopGenres.get(index)))
                    topTenRecommendedMedia.add(unlistenedMediaFromTopGenres.get(index));
            }
        }

        if (unlistenedMediaFromOtherGenres.size() <= 2) {
            numberOfMediasToFind = numberOfMediasToFind + 2 - unlistenedMediaFromOtherGenres.size();
            topTenRecommendedMedia.addAll(unlistenedMediaFromOtherGenres);
        }
        else{
            Random random = new Random();
            int index = 0;
            while(topTenRecommendedMedia.size() < 10){
                index = random.nextInt(unlistenedMediaFromOtherGenres.size());
                if(!topTenRecommendedMedia.contains(unlistenedMediaFromOtherGenres.get(index)))
                    topTenRecommendedMedia.add(unlistenedMediaFromOtherGenres.get(index));
            }
        }

        if(numberOfMediasToFind > 0){
            List<MediaVO> allMedia = this.getAllMedia();
            // Get all medias JOHANN, DET ÄR DETTA ENDPOINT SOM FATTAS!!
            List<MediaVO> listOfMedia = getListOfAllMedia();
            // Remove all media with thummbs down
            listOfMedia = removeMediaWithThumbDown(mediaUser, listOfMedia);
            // Choose missing media from the rest
            listOfMedia = getRandomListOfMedia(listOfMedia, numberOfMediasToFind);
            // Add them to topTenRecommended media
            topTenRecommendedMedia.addAll(listOfMedia);
        }

        return topTenRecommendedMedia;

/*
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
*/
    }

    //private List<GenreVO> getTopThreeMostPlayedGenresFromMediaHistory(List<StreamHistory> streamHistories){
    public List<Long> getTopThreeMostPlayedGenresFromMediaHistory(List<StreamHistory> streamHistories){
        List<GenreVO> genres = new ArrayList<>();
        boolean genreAdded;
        for (StreamHistory stream: streamHistories) {
            MediaVO mediaVO = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/media/" + stream.getMediaId(), MediaVO.class);
            if (mediaVO != null) {
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
        }
        genres.sort(Comparator.comparingInt(GenreVO::getCount).reversed());
        return genres.stream()
                .map(GenreVO::getId)
                .limit(3)
                .toList();
    }

    // Metod för att hitta media som användaren inte har lyssnat på baserat på genrer
    public List<MediaVO> findUnlistenedMediaFromTopGenres(Long userId, List<Long> genreIds) {
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);
        List<Long> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .toList();

        List<MediaVO> unlistenedMedia = new ArrayList<>();
        for (Long genreId : genreIds) {
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
    public List<MediaVO> findUnlistenedMediaFromOtherGenres(Long userId, List<Long> excludeGenreIds) {
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);
        List<Long> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .toList();

        List<MediaVO> unlistenedMedia = new ArrayList<>();
        GenreVO[] allGenres = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genres", GenreVO[].class);

        if (allGenres != null) {
            List<GenreVO> otherGenres = Arrays.stream(allGenres)
                    .filter(genreVO -> !excludeGenreIds.contains(genreVO.getId()))
                    .toList();

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

    public List<MediaVO> getAllMedia(){
        ResponseEntity<List<MediaVO>> responseEntity = restTemplate.exchange(
                "http://MEDIAMICROSERVICE/media/media/getall",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MediaVO>>() {}
        );
        return responseEntity.getBody();

    }

    private List<MediaVO> removeMediaWithThumbDown(MediaUser mediaUser, List<MediaVO> medias) {
        List<ThumbsUpAndDown> listOfMediaWithThumbDown = thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(mediaUser, true);
        List<Long> listOfMediaIdWithThumbDown = listOfMediaWithThumbDown.stream().map(ThumbsUpAndDown::getMediaId).toList();
        return medias.stream()
                .filter(mediaVO -> !listOfMediaIdWithThumbDown.contains(mediaVO.getId()))
                .toList();
    }

    // Metod för att hitta media som användaren inte har lyssnat på baserat på genrer
    public List<MediaVO> getListOfAllMedia() {
        MediaVO[] listOfAllMedia = restTemplate.getForObject(
                "http://MEDIAMICROSERVICE/media/media/getall", MediaVO[].class);
        return new ArrayList<MediaVO>(Arrays.asList(listOfAllMedia));
    }
        // Metod för att hitta media som användaren inte har lyssnat på baserat på genrer
    public List<MediaVO> getRandomListOfMedia(List<MediaVO> listOfMedia, int numberOfMediaToFind) {
        List<MediaVO> listOfRandomMedia = new ArrayList<>();

        if (listOfMedia != null) {
            if(listOfMedia.size() <= numberOfMediaToFind) {
                listOfRandomMedia.addAll(listOfMedia);
            }
            else{
                Random random = new Random();
                int index = 0;
                while(listOfRandomMedia.size() < numberOfMediaToFind){
                    index = random.nextInt(listOfMedia.size());
                    if(!listOfRandomMedia.contains(listOfMedia.get(index)))
                        listOfRandomMedia.add(listOfMedia.get(index));
                }
            }
        }
        return listOfRandomMedia;
    }

/*
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
        GenreVO[] allGenres = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genres", GenreVO[].class);

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
*/
}
