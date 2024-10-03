package com.project;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class WebPage {
    private final Semaphore semaphore;

    public WebPage(int maxConnections) {
        this.semaphore = new Semaphore(maxConnections);
    }

    public void connect(int userId) throws InterruptedException {
        if (semaphore.availablePermits() == 0) {
            System.out.println("Usuari " + userId + " espera. Pàgina saturada.");
        }
        semaphore.acquire();
        System.out.println("Usuari " + userId + " ha accedit a la pàgina web.");
    }

    public void disconnect(int userId) {
        System.out.println("Usuari " + userId + " s'ha desconnectat de la pàgina web.");
        semaphore.release();
    }
}