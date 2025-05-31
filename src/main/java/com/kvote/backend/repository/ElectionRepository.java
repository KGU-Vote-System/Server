package com.kvote.backend.repository;

import com.kvote.backend.domain.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {
    boolean existsById(Long id);
    Optional<Election> findById(Long id);
}