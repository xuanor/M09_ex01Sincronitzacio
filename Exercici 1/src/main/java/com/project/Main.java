package com.project;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        final int[] dades = {2, 4, 3, 5, 4, 12, 10, 4, 5, 2, 7, 7, 8}; 
        float[] results = new float[3]; // Para almacenar la suma, la media y la desviación estándar.

        // Creem un CyclicBarrier per a 3 fils
        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("Tots els microserveis han acabat. Combinant els resultats...");
                System.out.println("Suma: " + results[0]);
                System.out.println("Mitjana: " + results[1]);
                System.out.println("Desviació estàndard: " + results[2]);
            }
        });

        // Executor per gestionar els fils
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Tasca per calcular la suma
        Runnable sumaRunnable = () -> {
            try {
                System.out.println("Fent la suma...");
                Thread.sleep(1000);

                float total = 0;
                for (int n : dades) {
                    total += n;
                }

                results[0] = total;
                barrier.await(); // Esperem que els altres fils acabin
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        // Tasca per calcular la mitjana
        Runnable mitjanaRunnable = () -> {
            try {
                System.out.println("Fent la mitjana...");
                Thread.sleep(1500);

                float resultat = results[0] / dades.length; // Calculem la mitjana
                results[1] = resultat;
                barrier.await(); // Esperem que els altres fils acabin
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        // Tasca per calcular la desviació estàndard
        Runnable desviacioRunnable = () -> {
            try {
                System.out.println("Fent la desviacio estandard...");
                Thread.sleep(2000);

                // Primer esperem que la mitjana estigui calculada
                float mitjana = results[1];
                float sumaQuadrats = 0;

                // Calculem la suma dels quadrats de les desviacions
                for (int n : dades) {
                    sumaQuadrats += Math.pow(n - mitjana, 2);
                }

                // Desviació estàndard
                float desviacio = (float) Math.sqrt(sumaQuadrats / dades.length);
                results[2] = desviacio;

                barrier.await(); // Esperem que els altres fils acabin
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        // Executem les tasques
        executor.submit(sumaRunnable);
        executor.submit(mitjanaRunnable);
        executor.submit(desviacioRunnable);

        // Tanquem l'executor
        executor.shutdown();
    }
}
