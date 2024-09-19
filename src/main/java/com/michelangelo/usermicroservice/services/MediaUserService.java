package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.GenreVO;
import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.VO.MediaWithStreamCountVO;
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
public class MediaUserService implements MediaUserServiceInterface {

    private final MediaUserRepository mediaUserRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public MediaUserService(MediaUserRepository mediaUserRepository, RestTemplate restTemplate) {
        this.mediaUserRepository = mediaUserRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public MediaUser getMediaUser(long id) {
        return mediaUserRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("MediaUser","id",id));
    }

    @Override
    public MediaUser getMediaUserByUserName(String userName) {
        return mediaUserRepository.findByUserName(userName).orElseThrow(()-> new ResourceNotFoundException("MediaUser","username",userName));
    }

    @Override
    public List<MediaWithStreamCountVO> getTopPlayedMedia(long userId) {
        List<MediaWithStreamCountVO> resultList = new ArrayList<>();
        MediaUser mediaUser = mediaUserRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("MediaUser", "id",userId));
        List<StreamHistory> streamHistory = mediaUser.getStreamHistory();
        if (streamHistory.isEmpty()) return resultList;
        streamHistory.sort(Comparator.comparingInt(StreamHistory::getStreamHistoryCount).reversed());
        for(int i=0; i < streamHistory.size() && i < 5; i++){
            MediaVO mediaVO = restTemplate.getForObject("http://MEDIAMICROSERVICE/media/media/" + streamHistory.get(i).getMediaId(), MediaVO.class);
            resultList.add(new MediaWithStreamCountVO(mediaVO,streamHistory.get(i).getStreamHistoryCount()));
        }

        return resultList;
    }


}
