import java.util.Random;
import java.util.concurrent.Semaphore;

public class Exercise2 {

    //3 fases de lavado - lavado(2) - enjuague(1) - secado(2)
    private static Semaphore washing = new Semaphore(2, true);
    private static Semaphore rinsing = new Semaphore(1, true);
    private static Semaphore drying = new Semaphore(2, true);

    public static class VehicleThread extends Thread {
        private int vehicleId; // Vehicle ID

        // Constructor para asignar el ID en orden desde el main
        public VehicleThread(int vehicleId) {
            this.vehicleId = vehicleId;
        }

        @Override
        public void run() {
            try {
                // Fase 1: Lavado con jabón
                // We use this boolean variable to not be showing all the time the same message meanwhile the vehicle is on waiting status
                boolean hasWaitedForWashing = false;
                // Loop  until a washing spot becomes free
                while (!washing.tryAcquire()) {
                    if (!hasWaitedForWashing) {
                        // We just print the message on the first time that the vehicle is waiting
                        System.out.println("El vehículo " + vehicleId + " ha llegado y está esperando para el lavado con jabón");
                        hasWaitedForWashing = true;
                    }
                    // We add a waiting time to have a more realistic result on the printed messages
                    Thread.sleep(100); 
                }
                System.out.println("El vehículo " + vehicleId + " está siendo lavado con jabón");
                Thread.sleep(new Random().nextInt(5000)); 
                washing.release();

                // Fase 2: Enjuague
                boolean hasWaitedForRinsing = false;
                while (!rinsing.tryAcquire()) {
                    if (!hasWaitedForRinsing) {
                        System.out.println("El vehículo " + vehicleId + " ha terminado el lavado con jabón y está esperando para enjuagar");
                        hasWaitedForRinsing = true;
                    }
                    Thread.sleep(100); 
                }
                System.out.println("El vehículo " + vehicleId + " está siendo enjuagado");
                Thread.sleep(new Random().nextInt(5000)); 
                rinsing.release();

                // Fase 3: Secado
                boolean hasWaitedForDrying = false;
                while (!drying.tryAcquire()) {
                    if (!hasWaitedForDrying) {
                        System.out.println("El vehículo " + vehicleId + " ha terminado el enjuague y está esperando para secar");
                        hasWaitedForDrying = true;
                    }
                    Thread.sleep(100); 
                }
                System.out.println("El vehículo " + vehicleId + " está siendo secado");
                Thread.sleep(new Random().nextInt(5000)); 
                drying.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Creating 30 threads representing vehicles
        int numThreads = 30;
        Thread[] threads = new Thread[numThreads];


        // We are going to assign an id to each vehicle on a secuencial way, to easily read the order of vehicles and their actions
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new VehicleThread(i); // Vehicle ID
            threads[i].start();
        }

        // Finishing all threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
