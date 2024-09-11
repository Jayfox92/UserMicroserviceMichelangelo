package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import com.michelangelo.usermicroservice.services.ThumbsUpAndDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class ThumbsUpAndDownController {

    @Autowired
    private ThumbsUpAndDownService thumbsUpAndDownService;

    // endpoint för att spara ner tumme upp & tumme ner för media
    /*
    Endpoint för att spara ner tumme upp & tumme ner för ett media objekt.
    Inkluderar ett media id.
    Skicka in ett användar id.
    Returnerar ett ThumbsUpAndDown objekt.
     */
    @PostMapping("/v2/saveThumbs/{id}")
    public ResponseEntity<ThumbsUpAndDown> saveThumbs(@RequestBody ThumbsUpAndDown thumbsUpAndDown, @PathVariable long id) {
        return new ResponseEntity<>(thumbsUpAndDownService.placeThumbsUpAndDown(thumbsUpAndDown, id), HttpStatus.OK); // alt CREATED
    }

    /*
    dela upp i ta bort, uppdatera, spara
    eller ha en endpoint som kontrollerar om det finns en tumme upp/ner redan
    för vald media
    ????
     */


}
