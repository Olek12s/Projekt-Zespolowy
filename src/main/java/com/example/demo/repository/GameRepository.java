package com.example.demo.repository;

import com.example.demo.model.Game;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    List<Game> findByWhitePlayerOrBlackPlayer(User whitePlayer, User blackPlayer);

    List<Game> findByWhitePlayer_IdOrBlackPlayer_IdOrderByCreatedAtDesc(
            String whitePlayerId,
            String blackPlayerId
    );

    List<Game> findTop5ByOrderByCreatedAtDesc();
    List<Game> findTop10ByOrderByCreatedAtDesc();
    List<Game> findTop15ByOrderByCreatedAtDesc();
    List<Game> findTop30ByOrderByCreatedAtDesc();
}