package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.VO.MediaWithStreamCountVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;

import java.util.List;

public interface MediaUserServiceInterface {
    public MediaUser getMediaUser(long id);
    public MediaUser getMediaUserByUserName(String userName);
    public List<MediaWithStreamCountVO> getTopPlayedMedia(long userId);
}
