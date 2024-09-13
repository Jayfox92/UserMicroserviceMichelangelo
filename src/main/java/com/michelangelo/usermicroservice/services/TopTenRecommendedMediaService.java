package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TopTenRecommendedMediaService implements TopTenRecommendedMediaServiceInterface {
    MediaUserRepository mediaUserRepository;
    RestTemplate restTemplate;

    @Autowired
    public TopTenRecommendedMediaService(MediaUserRepository mediaUserRepository, RestTemplate restTemplate) {
        this.mediaUserRepository = mediaUserRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MediaVO> getTopTenRecommendedMedia(Long userId) {
        MediaUser mediaUser = mediaUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("MediaUser", "id", userId));
        List<GenreVO> genres = getTopThreeMostPlayedGenresFromMediaHistory(mediaUser.getStreamHistory());
        return null;
    }

    private List<GenreVO> getTopThreeMostPlayedGenresFromMediaHistory(List<StreamHistory> streamHistory){
        List<GenreVO> genres = new ArrayList<>();
        boolean genreAdded;
        for (StreamHistory stream: streamHistory) {
            MediaVO mediaVO = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/" + stream.getMediaId(), MediaVO.class);
            for (GenreVO genreVO: mediaVO.getGenres()) {
                genreAdded = false;
                for (GenreVO genre: genres) {
                    if (genre.getId().equals(genreVO.getId())) {
                        genre.setCount(genre.getCount() + stream.getStreamHistoryCount());
                        genreAdded = true;
                    }
                }
                if (!genreAdded){
                    genres.add(genreVO);
                    genres.getLast().setCount(stream.getStreamHistoryCount());
                }
            }
        }
        genres.sort(Comparator.comparingInt(GenreVO::getCount));
        return genres;
    }
}
