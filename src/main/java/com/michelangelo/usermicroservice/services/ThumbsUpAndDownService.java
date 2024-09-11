package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import com.michelangelo.usermicroservice.repositories.ThumbsUpAndDownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThumbsUpAndDownService implements ThumbsUpAndDownServiceInterface {

    @Autowired
    public ThumbsUpAndDownRepository thumbsUpAndDownRepository;

    @Autowired
    public MediaUserRepository mediaUserRepository;



    @Override
    public ThumbsUpAndDown placeThumbsUpAndDown(ThumbsUpAndDown thumbsUpAndDown, long id) {
        // Hämta användaren om användaren finns
        MediaUser mediaUser = mediaUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MediaUser", "id", id));

        // Hämta listan med tumme upp och ned
        List<ThumbsUpAndDown> userList = mediaUser.getThumbsUpAndDown();

        // Kontrollera om det redan finns en tumme upp eller ner på media id
        // long mediaId


        // Om finns - ändra den som finns
        // Om inte - lägg till en ny i listan

        // spara till databasen och skicka tillbaka ThumbsUpAndDown objekt


        return null;
    }
}
