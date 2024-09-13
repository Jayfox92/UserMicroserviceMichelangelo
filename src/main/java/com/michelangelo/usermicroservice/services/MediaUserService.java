package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.exceptions.ResourceNotFoundException;
import com.michelangelo.usermicroservice.repositories.MediaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaUserService implements MediaUserServiceInterface {

    private final MediaUserRepository mediaUserRepository;

    @Autowired
    public MediaUserService(MediaUserRepository mediaUserRepository) {
        this.mediaUserRepository = mediaUserRepository;
    }

    @Override
    public MediaUser getMediaUser(long id) {
        return mediaUserRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("MediaUser","id",id));
    }

    @Override
    public MediaUser getMediaUserByUserName(String userName) {
        return mediaUserRepository.findByUserName(userName).orElseThrow(()-> new ResourceNotFoundException("MediaUser","username",userName));
    }

}
