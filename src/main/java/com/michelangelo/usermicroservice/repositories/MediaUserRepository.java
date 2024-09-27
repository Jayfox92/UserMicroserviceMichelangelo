package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaUserRepository extends JpaRepository<MediaUser, Long> {
    Optional<MediaUser> findByUserName(String userName);
}
