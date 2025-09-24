package com.inf1nlty.soultotem.util;

import java.util.ArrayDeque;
import java.util.Queue;

public class PotionEffectQueue {
    private static final Queue<Runnable> queue = new ArrayDeque<>();

    public static void enqueue(Runnable potionTask) {
        queue.add(potionTask);
    }

    public static void flush() {
        while (!queue.isEmpty()) queue.poll().run();
    }
}