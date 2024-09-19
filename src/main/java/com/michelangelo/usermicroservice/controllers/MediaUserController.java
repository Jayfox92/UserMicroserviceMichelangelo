package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.VO.MediaVO;
import com.michelangelo.usermicroservice.VO.MediaWithStreamCountVO;
import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.services.MediaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/mediauser")
public class MediaUserController {
    @Autowired
    private MediaUserService mediaUserService;

    @GetMapping("/getuser/{id}")
    public MediaUser getMediaUser(@PathVariable("id") long id){
        return mediaUserService.getMediaUser(id);
    }

    @GetMapping("/getuserbyusername/{username}")
    public MediaUser getMediaUserByUserName(@PathVariable String username){
        return mediaUserService.getMediaUserByUserName(username);
    }

    @GetMapping("/gettopplayedmedia/{userId}")
    public ResponseEntity<List<MediaWithStreamCountVO>> getTopplayedMediaByUserId(@PathVariable long userId){

        return ResponseEntity.ok(mediaUserService.getTopPlayedMedia(userId));
    }


}
