package com.green.poolstatsapi.repository;

import com.green.poolstatsapi.models.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {
}
