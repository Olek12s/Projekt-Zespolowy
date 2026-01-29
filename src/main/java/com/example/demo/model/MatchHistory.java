package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_history")
@Getter @Setter @NoArgsConstructor
public class MatchHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "white_id", nullable = false)
    private User whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_id", nullable = false)
    private User blackPlayer;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String notation;

}
