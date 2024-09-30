package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.MediaWithStreamCountVO;
import com.michelangelo.usermicroservice.entities.MediaUser;

import java.util.List;

public interface MediaUserServiceInterface {
     MediaUser getMediaUser(long id);
     MediaUser getMediaUserByUserName(String userName);
     List<MediaWithStreamCountVO> getTopPlayedMedia(long userId, int limit);
}
