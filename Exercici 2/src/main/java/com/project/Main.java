package com.project;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        int parkingCapacity = 5; 
        ParkingLot parkingLot = new ParkingLot(parkingCapacity);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            int carId = i;
            executor.submit(() -> {
                try {
                    parkingLot.enterParking(carId); 
                    Thread.sleep((long) (Math.random() * 5000)); 
                    parkingLot.exitParking(carId);  
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

class ParkingLot {
    private final Semaphore semaphore;

    public ParkingLot(int capacity) {
        this.semaphore = new Semaphore(capacity); 
    }

    public void enterParking(int carId) throws InterruptedException {
        if (semaphore.availablePermits() == 0) {
            System.out.println("Cotxe " + carId + " espera. Aparcament ple.");
        }
        semaphore.acquire(); 
        System.out.println("Cotxe " + carId + " entra a l'aparcament.");
    }
    public void exitParking(int carId) {
        System.out.println("Cotxe " + carId + " surt de l'aparcament.");
        semaphore.release(); 
    }
}
