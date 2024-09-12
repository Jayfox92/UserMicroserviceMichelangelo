package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.ThumbsUpAndDownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ThumbsUpAndDownService implements ThumbsUpAndDownServiceInterface {

    private final ThumbsUpAndDownRepository thumbsUpAndDownRepository;
    private final MediaUserRepository mediaUserRepository;

    @Autowired
    public ThumbsUpAndDownService(ThumbsUpAndDownRepository thumbsUpAndDownRepository, MediaUserRepository mediaUserRepository) {
        this.thumbsUpAndDownRepository = thumbsUpAndDownRepository;
        this.mediaUserRepository = mediaUserRepository;
    }

    @Override
    public ThumbsUpAndDown placeThumbsUpAndDown(ThumbsUpAndDown thumbsUpAndDown) {
        // Kontrollera att att inmatningen inte både innehåller true på tumme upp och ner
        if(thumbsUpAndDown.isThumbsUp() && thumbsUpAndDown.isThumbsDown()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Both thumbsUp and thumbsDown cannot both be true.");
        }

        // Hämta användare och listan för användaren
        MediaUser mediaUser = mediaUserRepository.findById(thumbsUpAndDown.getMediaUser().getId()).orElseThrow(() -> new ResourceNotFoundException("MediaUser", "id", thumbsUpAndDown.getMediaUser().getId()));
        List<ThumbsUpAndDown> userList = mediaUser.getThumbsUpAndDown();

        // Kontrollera om det redan finns en tumme upp eller ner på valt media id
        ThumbsUpAndDown existingThumbs = null;
        for(ThumbsUpAndDown t : userList) {
            if(t.getMediaId() == thumbsUpAndDown.getMediaId()) {
                existingThumbs = t;
                break;
            }
        }

        if(existingThumbs != null) { // Om det finns en tumme upp eller ner  - uppdatera den som finns
            existingThumbs.setThumbsUp(thumbsUpAndDown.isThumbsUp());
            existingThumbs.setThumbsDown(thumbsUpAndDown.isThumbsDown());
            thumbsUpAndDownRepository.save(existingThumbs);
            return existingThumbs;
        } else { // Om det inte finns en tumme upp eller ner i listan för media id - lägga till en ny
            thumbsUpAndDown.setMediaUser(mediaUser);
            mediaUser.getThumbsUpAndDown().add(thumbsUpAndDown);
            mediaUserRepository.save(mediaUser);
            return thumbsUpAndDown;
        }
    }

}
