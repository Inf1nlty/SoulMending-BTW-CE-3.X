package com.inf1nlty.soulmending.client;

import java.util.ArrayDeque;
import java.util.Queue;

public class ParticleSpawnQueue {

    private static final Queue<Runnable> queue = new ArrayDeque<>();

    public static void enqueue(Runnable particleTask) {
        queue.add(particleTask);
    }

    public static void flush() {
        while (!queue.isEmpty()) queue.poll().run();
    }
}