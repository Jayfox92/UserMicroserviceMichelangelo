package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.ThumbsUpAndDown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbsUpAndDownRepository extends JpaRepository<ThumbsUpAndDown, Long> {
}
