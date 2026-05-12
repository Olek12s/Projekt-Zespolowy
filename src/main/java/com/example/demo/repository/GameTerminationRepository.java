package com.example.demo.repository;

import com.example.demo.model.GameTermination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameTerminationRepository extends JpaRepository<GameTermination, String> {
    Optional<GameTermination> findByTermination(String termination);
}