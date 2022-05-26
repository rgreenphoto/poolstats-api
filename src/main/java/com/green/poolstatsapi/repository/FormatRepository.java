package com.green.poolstatsapi.repository;

import com.green.poolstatsapi.models.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FormatRepository extends JpaRepository<Format, Long> {
}
