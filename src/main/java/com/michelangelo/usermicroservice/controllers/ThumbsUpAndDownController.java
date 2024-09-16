package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import com.michelangelo.usermicroservice.services.ThumbsUpAndDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/thumbs")
public class ThumbsUpAndDownController {

    @Autowired
    private ThumbsUpAndDownService thumbsUpAndDownService;

    // Endpoint för att spara ner tumme upp & tumme ner för vald media
    @PostMapping("/savethumbs")
    public ResponseEntity<ThumbsUpAndDown> saveThumbs(@RequestBody ThumbsUpAndDown thumbsUpAndDown) {
        return new ResponseEntity<>(thumbsUpAndDownService.placeThumbsUpAndDown(thumbsUpAndDown), HttpStatus.OK);
    }


}
