package com.green.poolstatsapi.repository;

import com.green.poolstatsapi.models.RackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackTypeRepository extends JpaRepository<RackType, Long> {
}
