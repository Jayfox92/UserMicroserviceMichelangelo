package com.michelangelo.usermicroservice.repositories;

import com.michelangelo.usermicroservice.entities.StreamHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreamHistoryRepository extends JpaRepository<StreamHistory,Long> {
    Optional<StreamHistory> findByMediaUser_IdAndMediaId(Long userId, Long mediaId);

}
