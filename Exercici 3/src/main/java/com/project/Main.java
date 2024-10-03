package com.project;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        int maxConnections = 3;
        WebPage webPage = new WebPage(maxConnections);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            int userId = i;
            executor.submit(() -> {
                try {
                    webPage.connect(userId);
                    Thread.sleep((long) (Math.random() * 5000));
                    webPage.disconnect(userId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




