package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.MediaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaUserRepository extends JpaRepository<MediaUser, Long> {
}
