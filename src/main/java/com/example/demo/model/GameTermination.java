package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_terminations")
public class GameTermination {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String termination;

    public GameTermination() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTermination() { return termination; }
    public void setTermination(String termination) { this.termination = termination; }
}