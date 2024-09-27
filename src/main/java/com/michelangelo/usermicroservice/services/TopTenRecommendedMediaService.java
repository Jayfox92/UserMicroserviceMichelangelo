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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


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
    public List<MediaVO> getTopTenRecommendedMedia(Long userId, String mediaType) {
        MediaUser mediaUser = mediaUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("MediaUser", "id", userId));
        List<Long> topGenres = getTopThreeMostPlayedGenresFromMediaHistory(mediaUser.getStreamHistory());

        // Hämta all media från top genrer
        List<MediaVO> unlistenedMediaFromTopGenres = findUnlistenedMediaFromTopGenres(userId, topGenres, mediaType);

        // Hämta all media från andra genrer
        List<MediaVO> unlistenedMediaFromOtherGenres = findUnlistenedMediaFromOtherGenres(userId, topGenres,mediaType);

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
            int amountToAdd = topTenRecommendedMedia.size()+2;
            while(topTenRecommendedMedia.size() < amountToAdd){
                index = random.nextInt(unlistenedMediaFromOtherGenres.size());
                if(!topTenRecommendedMedia.contains(unlistenedMediaFromOtherGenres.get(index)))
                    topTenRecommendedMedia.add(unlistenedMediaFromOtherGenres.get(index));
            }
        }

        if(numberOfMediasToFind > 0){
//            List<MediaVO> allMedia = this.getAllMedia();
            // Get all medias JOHANN, DET ÄR DETTA ENDPOINT SOM FATTAS!!
            List<MediaVO> listOfMedia = getListOfAllMedia(mediaType);
            // Remove all media with thummbs down
            listOfMedia = removeMediaWithThumbDown(mediaUser, listOfMedia);
            // Choose missing media from the rest
            listOfMedia = getRandomListOfMedia(listOfMedia, numberOfMediasToFind);
            // Add them to topTenRecommended media
            topTenRecommendedMedia.addAll(listOfMedia);
        }

        return topTenRecommendedMedia;

    }

    //private List<GenreVO> getTopThreeMostPlayedGenresFromMediaHistory(List<StreamHistory> streamHistories){
    public List<Long> getTopThreeMostPlayedGenresFromMediaHistory(List<StreamHistory> streamHistories){
        List<GenreVO> genres = new ArrayList<>();
        boolean genreAdded;
        for (StreamHistory stream: streamHistories) {
            MediaVO mediaVO;
            try{
                mediaVO = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/media/" + stream.getMediaId(), MediaVO.class);
            }catch(RestClientException | IllegalStateException e){
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "MediaMicroservice: " + e.getMessage(), e);
            }

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
    public List<MediaVO> findUnlistenedMediaFromTopGenres(Long userId, List<Long> genreIds,String mediaType) {
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);
        List<Long> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .toList();

        List<MediaVO> unlistenedMedia = new ArrayList<>();
        for (Long genreId : genreIds) {
            MediaVO[] mediaFromGenre;
            try{
                mediaFromGenre = restTemplate.getForObject(
                        "http://MEDIAMICROSERVICE/media/media/genre/" + genreId + "/" + mediaType, MediaVO[].class);
            }catch(RestClientException | IllegalStateException e){
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "MediaMicroservice: " + e.getMessage(), e);
            }


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
    public List<MediaVO> findUnlistenedMediaFromOtherGenres(Long userId, List<Long> excludeGenreIds,String mediaType) {
        List<StreamHistory> userStreamHistory = streamHistoryRepository.findByMediaUser_Id(userId);
        List<Long> listenedMediaIds = userStreamHistory.stream()
                .map(StreamHistory::getMediaId)
                .toList();

        List<MediaVO> unlistenedMedia = new ArrayList<>();
        GenreVO[] allGenres;
        try{
            allGenres = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/genre/getall", GenreVO[].class);
        }catch(RestClientException | IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "MediaMicroservice: " + e.getMessage(), e);
        }

        if (allGenres != null) {
            List<GenreVO> otherGenres = Arrays.stream(allGenres)
                    .filter(genreVO -> !excludeGenreIds.contains(genreVO.getId()))
                    .toList();

            for (GenreVO genreVO : otherGenres) {
                MediaVO[] mediaFromGenre;
                try{
                    mediaFromGenre= restTemplate.getForObject(
                            "http://MEDIAMICROSERVICE/media/media/genre/" + genreVO.getId() + "/" + mediaType, MediaVO[].class);
                }catch(RestClientException | IllegalStateException e){
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "MediaMicroservice: " + e.getMessage(), e);
                }


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

    private List<MediaVO> removeMediaWithThumbDown(MediaUser mediaUser, List<MediaVO> medias) {
        List<ThumbsUpAndDown> listOfMediaWithThumbDown = thumbsUpAndDownRepository.findAllByMediaUserAndThumbsDown(mediaUser, true);
        List<Long> listOfMediaIdWithThumbDown = listOfMediaWithThumbDown.stream().map(ThumbsUpAndDown::getMediaId).toList();
        return medias.stream()
                .filter(mediaVO -> !listOfMediaIdWithThumbDown.contains(mediaVO.getId()))
                .toList();
    }

    // Metod för att hitta media som användaren inte har lyssnat på baserat på genrer
    public List<MediaVO> getListOfAllMedia(String mediaType) {
        MediaVO[] listOfAllMedia;
        try{
            listOfAllMedia = restTemplate.getForObject(
                    "http://MEDIAMICROSERVICE/media/media/getallbymediatype/"+mediaType, MediaVO[].class);
        }catch(RestClientException | IllegalStateException e){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "MediaMicroservice: " + e.getMessage(), e);
        }

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
}
