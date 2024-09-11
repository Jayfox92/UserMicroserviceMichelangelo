package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.services.MediaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/user")
public class MediaUserController {
    @Autowired
    private MediaUserService mediaUserService;

    @GetMapping("/getuser/{id}")
    public MediaUser getMediaUser(@PathVariable("id") long id){
        return mediaUserService.getMediaUser(id);
    }
}
