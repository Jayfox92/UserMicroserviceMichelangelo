package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.services.TopTenRecommendedMediaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/topten")
public class TopTenRecommendedMediaController {
    TopTenRecommendedMediaServiceInterface topTenRecommendedMediaService;

    @Autowired
    public TopTenRecommendedMediaController(TopTenRecommendedMediaServiceInterface topTenRecommendedMediaService) {
        this.topTenRecommendedMediaService = topTenRecommendedMediaService;
    }

    @GetMapping("/gettoptenrecommendedmedia/{id}")
    public ResponseEntity<List<MediaVO>> getTopTenRecommendedMedia(@PathVariable long id){
        return ResponseEntity.ok(topTenRecommendedMediaService.getTopTenRecommendedMedia(id));
    }
}
