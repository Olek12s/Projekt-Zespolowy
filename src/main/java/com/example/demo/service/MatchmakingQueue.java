package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

@Service
public class MatchmakingQueue {

    public record QueueEntry(String username, double elo, long joinedAt) {}

    private static final int ELO_RANGE = 200;

    private final PriorityQueue<QueueEntry> queue = new PriorityQueue<>(
            Comparator.comparingDouble(QueueEntry::elo)
    );

    public synchronized void add(QueueEntry entry) {
        queue.add(entry);
    }

    public synchronized void remove(String username) {
        queue.removeIf(e -> e.username().equals(username));
    }

    public synchronized boolean isInQueue(String username) {
        return queue.stream().anyMatch(e -> e.username().equals(username));
    }

    public synchronized Optional<QueueEntry> findMatch(double elo) {
        return queue.stream().filter(e -> {
                    long waitSeconds = (System.currentTimeMillis() - e.joinedAt()) / 1000;
                    int range;
                    if (waitSeconds < 10) {
                        range = 200;
                    } else if (waitSeconds < 30) {
                        range = 400;
                    } else {
                        range = 800;
                    }
                    return Math.abs(e.elo() - elo) <= range;
                }).findFirst();
    }
}