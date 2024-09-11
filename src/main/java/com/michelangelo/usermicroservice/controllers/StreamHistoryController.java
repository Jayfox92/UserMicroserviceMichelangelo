package com.michelangelo.usermicroservice.controllers;

import com.michelangelo.usermicroservice.entities.StreamHistory;
import com.michelangelo.usermicroservice.services.StreamHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/streamhistory")
public class StreamHistoryController {
    @Autowired
    private StreamHistoryService streamHistoryService;

    @PutMapping("/increment/{userId}/{mediaId}")
    public ResponseEntity<StreamHistory> incrementStreamHistory(@PathVariable("userId") long userId, @PathVariable("mediaId") long mediaId){
        return ResponseEntity.ok(streamHistoryService.incrementStreamHistory(userId,mediaId));
    }

}
