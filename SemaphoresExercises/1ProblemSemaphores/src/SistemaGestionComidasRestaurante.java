
import java.util.Random;
import java.util.concurrent.Semaphore;

public class SistemaGestionComidasRestaurante {

    //VARIABLES COMPARTIDAS SON LOS MISMOS SEMAFOROS EN ESTE CASO
    // Mostrador con capacidad de 5 platos (semaforo que limita el acceso a 5).
    // BandConveyor - 10 sweets limit
    private static Semaphore foodCounter = new Semaphore(5, true);

    // Semáforo para rastrear la cantidad de platos disponibles en el mostrador (inicialmente 0).
    // Tracking how much food is available on the kitchen counter
    private static Semaphore availableFood = new Semaphore(0, true);

     // Objeto para sincronizar los mensajes.
     //Mejora la claridad en la salida de consola y garantiza que no se intercalen los mensajes de diferentes hilos.
    //  private static final Object messageLock = new Object();

    // Clase interna para el hilo productor (Tipo 0).
    public static class Type0Thread extends Thread {

        private int id; // ID del hilo

        // Constructor que asigna el ID al hilo productor.
        // Constructor ID
        public Type0Thread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // Thread starting, productor is making food
                //synchronized (messageLock) {
                    System.out.println("El cocinero " + id + " está preparando un plato");
                //}
                // We use this boolean variable to not be showing all the time the same message meanwhile the vehicle is on waiting status
                boolean hasWaitedForWashing = false;
                // El productor intenta adquirir un permiso del semáforo del mostrador de comida.
                // Getting the foodCounter sempahore key if its possible
                while (!foodCounter.tryAcquire()) {

                    if (!hasWaitedForWashing) {
                        // We just print the message on the first time that the vehicle is waiting
                        //synchronized (messageLock) {
                            System.out.println("El cocinero " + id + " debe esperar, el mostrador está lleno");
                        //}
                        hasWaitedForWashing = true;
                    }

                    Thread.sleep(100);
                }

                // Simulamos un tiempo de producción aleatorio (hasta 5000 ms).
                // Random production time
                int sleepTime = new Random().nextInt(5000);
                Thread.sleep(sleepTime);

                // Mensaje indicando que el productor ha producido un plato.
                // Productor has endend the food
                //synchronized (messageLock) {
                    System.out.println("El cocinero " + id + " ha colocado un plato en el mostrador");
                //}
                // El productor libera un plato, indicando que hay un plato disponible para entregar.
                // Releasing the food, It is available to serve
                availableFood.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Clase interna para el hilo consumidor (Tipo 1).
    public static class Type1Thread extends Thread {

        private int id;

        public Type1Thread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // Lo hacemos con tryAcquire porque el enunciado menciona explicitamente que mientras el consumidor espera, deba hacer algo como por ejemplo decir que esta esperando
                // Simplemente el consumidor espera a que haya un plato
                // El consumidor intenta adquirir un plato del mostrador.
                // Consumer trying to get food from the counter
                while (!availableFood.tryAcquire()) {

                    //synchronized (messageLock) {
                        System.out.println("El repartirdor " + id + " debe esperar, no hay platos en el mostrador");
                    //}
                    Thread.sleep(100);
                }

                //Consumer taking food
                //synchronized (messageLock) {
                    System.out.println("El repartidor " + id + " está cogiendo un plato");
                //}

                int sleepTime = new Random().nextInt(5000);
                Thread.sleep(sleepTime);

                //synchronized (messageLock) {
                    System.out.println("El repartidor " + id + " ha entregado el plato");
                //}

                // El consumidor libera un permiso en el mostrador, indicando que se ha retirado un plato.
                // Consumer release a key from the counter
                foodCounter.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int numThreads = 30;
        Thread[] threads = new Thread[numThreads];
        Random random = new Random();

        // Bucle para crear e iniciar los hilos.
        for (int i = 0; i < numThreads; i++) {
            // Generar un hilo aleatoriamente como productor o consumidor.
            if (random.nextBoolean()) {
                threads[i] = new Type0Thread(i);
            } else {
                threads[i] = new Type1Thread(i);
            }
            threads[i].start(); // Iniciar el hilo
        }

        // Bucle para esperar a que todos los hilos terminen.
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

