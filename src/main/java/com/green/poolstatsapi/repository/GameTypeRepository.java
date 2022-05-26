package com.green.poolstatsapi.repository;

import com.green.poolstatsapi.models.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameTypeRepository extends JpaRepository<GameType, Long> {
}
