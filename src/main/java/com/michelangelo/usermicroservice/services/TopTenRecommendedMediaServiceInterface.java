package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.VO.MediaVO;

import java.util.List;

public interface TopTenRecommendedMediaServiceInterface {
    List<MediaVO> getTopTenRecommendedMedia(Long userId, String mediaType);
}
