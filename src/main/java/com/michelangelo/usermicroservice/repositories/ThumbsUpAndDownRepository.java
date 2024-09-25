package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThumbsUpAndDownRepository extends JpaRepository<ThumbsUpAndDown, Long> {

    List<ThumbsUpAndDown> findAllByMediaUserAndThumbsDown(MediaUser mediaUser, boolean thumbsDown);
}
