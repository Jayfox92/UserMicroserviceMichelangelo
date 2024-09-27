package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.exceptions.CustomErrorResponse;
import com.michelangelo.usermicroservice.services.TopTenRecommendedMediaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/user/topten")
public class TopTenRecommendedMediaController {
    TopTenRecommendedMediaServiceInterface topTenRecommendedMediaService;

    @Autowired
    public TopTenRecommendedMediaController(TopTenRecommendedMediaServiceInterface topTenRecommendedMediaService) {
        this.topTenRecommendedMediaService = topTenRecommendedMediaService;
    }

    @GetMapping("/gettoptenrecommendedmedia/{userId}/{mediaType}")
    public ResponseEntity<List<MediaVO>> getTopTenRecommendedMedia(@PathVariable long userId, @PathVariable String mediaType){
        return ResponseEntity.ok(topTenRecommendedMediaService.getTopTenRecommendedMedia(userId,mediaType));
    }

    // Hanterar ResponseStatusExceptions med en tydligare respons av CustomErrorResponse
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomErrorResponse> handleExceptionResponse(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new CustomErrorResponse(ex.getStatusCode(),  ex.getMessage()));
    }
}
