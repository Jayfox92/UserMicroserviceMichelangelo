package com.michelangelo.usermicroservice.services;

import com.michelangelo.usermicroservice.entities.MediaUser;

public interface MediaUserServiceInterface {
    public MediaUser getMediaUser(long id);
    public MediaUser getMediaUserByUserName(String userName);
}
