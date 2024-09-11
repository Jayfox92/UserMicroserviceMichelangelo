package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaUserService implements MediaUserServiceInterface {
    @Autowired
    private MediaUserRepository mediaUserRepository;

    @Override
    public MediaUser getMediaUser(long id) {
        return mediaUserRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("MediaUser","id",id));
    }
}
