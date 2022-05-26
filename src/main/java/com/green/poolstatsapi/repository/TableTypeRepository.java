package com.green.poolstatsapi.repository;

import com.green.poolstatsapi.models.TableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TableTypeRepository extends JpaRepository<TableType, Long> {
}
