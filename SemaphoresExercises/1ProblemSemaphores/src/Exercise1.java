
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Exercise1 {

    // 3 surtidores - 2 de gasolina simultáneamente - 1 de diesel
    private static Semaphore gasolinaSemaphore = new Semaphore(2,true);
    private static Semaphore dieselSemaphore = new Semaphore(1, true);
    private static List<String> drivers = new ArrayList<>(List.of("Toni", "Alvaro", "Maria", "Marc", "Alba", "Abi", "Carlos", "Damian", "Jodi", "Pepe", "Antonio", "Saray", "Domingo", "Paula", "Ibai", "Ruben", "Jessica", "Paco", "Sergio", "Victor"));

    // Method to select a random driver and delete it from the list
    public static String selectAndRemoveDriver() {
        Random random = new Random();
        int randomIndex = random.nextInt(drivers.size());
        String selectedDriver = drivers.get(randomIndex);
        drivers.remove(randomIndex);
        return selectedDriver;
    }

    public static class Type0Thread extends Thread {

        @Override
        public void run() {
            try {
                // Cuando se genera, mostrar mensaje "El conductor X con coche de Gasolina ha llegado"
                String driver = selectAndRemoveDriver();
                System.out.println("El conductor " + driver + " con coche de Gasolina ha llegado");

                //Trying to acces to a petrol pump if its free
                if (!gasolinaSemaphore.tryAcquire()) {
                    //If it is busy, show message and wait (not ordered... SOLVING THIS)
                    //second argument set to true, as in new Semaphore(2, true), it ensures fair access to the resources (pumps). 
                    //This means that threads (drivers) that arrive first will be given priority over those that arrive later.
                    System.out.println("El conductor " + driver + " con coche de Gasolina debe esperar, el surtidor está ocupado.");
                    gasolinaSemaphore.acquire();
                }

                System.out.println("El conductor " + driver + " con coche de Gasolina está repostando");
                int sleepTime = new Random().nextInt(5000); // Refuelling time
                Thread.sleep(sleepTime);

                // Finishing refuelling
                System.out.println("El conductor " + driver + " con coche de Gasolina ha terminado de repostar.");
                gasolinaSemaphore.release(); // Letting the petrol pump free
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Type1Thread extends Thread {

        @Override
        public void run() {
            try {

                String driver = selectAndRemoveDriver();
                System.out.println("El conductor " + driver + " con coche de Diesel ha llegado");

                if (!dieselSemaphore.tryAcquire()) {

                    System.out.println("El conductor " + driver + " con coche de Diesel debe esperar, el surtidor está ocupado.");
                    dieselSemaphore.acquire();
                }

                System.out.println("El conductor " + driver + " con coche de Diesel está repostando");
                int sleepTime = new Random().nextInt(5000);
                Thread.sleep(sleepTime);

                System.out.println("El conductor " + driver + " con coche de Diesel ha terminado de repostar.");
                dieselSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Creating 20 theards in a random way, type 0 or type1.
        int numThreads = 20;
        Thread[] threads = new Thread[numThreads];
        Random random = new Random();

        for (int i = 0; i < numThreads; i++) {
            if (random.nextBoolean()) {
                threads[i] = new Type0Thread();
            } else {
                threads[i] = new Type1Thread();
            }
            threads[i].start();
        }
    }
}
