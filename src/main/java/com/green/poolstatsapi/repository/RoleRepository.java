package com.green.poolstatsapi.repository;

import com.green.poolstatsapi.models.ERole;
import com.green.poolstatsapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
