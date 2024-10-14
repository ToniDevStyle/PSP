import java.util.Random;
import java.util.concurrent.Semaphore;

public class Barbero {

    // 1 Silla de barbero y 3 de espera (4 sillas total)
    private static Semaphore waitingChairs = new Semaphore(3); // 3 sillas de espera
    private static Semaphore barberChair = new Semaphore(1); // 1 silla de barbero
    private static Semaphore sleepingBarber = new Semaphore(0); // Para que el barbero duerma
    private static boolean busyBarber = false; // Variable de control para verificar si el barbero está ocupado

    static class BarberoThread extends Thread {
        private int currentClient = -1;  // To store the ID of the current client being served
    
        @Override
        public void run() {
            while (true) {
                try {
                    // Barber waits until a client wakes him up
                    sleepingBarber.acquire();
    
                    // Barber is now cutting hair
                    System.out.println("El barbero está cortando el cabello del cliente " + currentClient + ".");
                    Thread.sleep(new Random().nextInt(1000) + 1000); // Simulate cutting hair time
    
                    System.out.println("El barbero ha terminado de cortar el cabello del cliente " + currentClient + ".");
                    currentClient = -1; // Reset after finishing the haircut
    
                    busyBarber = false; // Barber is now free
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    barberChair.release(); // Release the barber chair after finishing
                }
            }
        }
    
        public void setCurrentClient(int clientId) {
            this.currentClient = clientId;  // Barber serves this client
        }
    }

    // Clase que extiende Thread para representar a los clientes
    static class ClienteThread extends Thread {

        private int id;
        private BarberoThread barbero; // Referencia al barbero
    
        public ClienteThread(int id, BarberoThread barbero) {
            this.id = id;
            this.barbero = barbero;  // Inicializar la referencia al barbero
        }
    
        @Override
        public void run() {
            System.out.println("[Cliente " + id + "] ha llegado a la barbería.");
    
            try {
                // Primero intentamos adquirir la silla del barbero directamente
                if (barberChair.tryAcquire()) {
                    // Si el barbero no está ocupado y la silla está libre, el cliente lo despierta
                    System.out.println("[Cliente " + id + "] despierta al barbero.");
                    barbero.setCurrentClient(id);  // Asignar el cliente al barbero
                    sleepingBarber.release(); // Despertar al barbero
                    System.out.println("[Cliente " + id + "] pasa a la silla del barbero.");
                } else if (waitingChairs.tryAcquire()) {
                    // Si la silla del barbero está ocupada, el cliente intenta sentarse en una silla de espera
                    System.out.println("[Cliente " + id + "] se sienta a esperar.");
    
                    // El cliente espera hasta que la silla del barbero esté disponible
                    barberChair.acquire(); // Espera hasta que la silla esté libre
                    waitingChairs.release(); // Libera la silla de espera
                    System.out.println("[Cliente " + id + "] pasa a la silla del barbero.");
                    barbero.setCurrentClient(id);  // Asignar el cliente al barbero
                    sleepingBarber.release(); // Despertar al barbero si está dormido
                } else {
                    // Si no hay sillas de espera disponibles, el cliente se va
                    System.out.println("[Cliente " + id + "] se va porque no hay sillas disponibles.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
        

        public static void main(String[] args) {
            int numClientes = 100;
            ClienteThread[] clientes = new ClienteThread[numClientes];
        
            // Instancia del barbero
            BarberoThread barbero = new BarberoThread();
        
            barbero.start(); // Arrancar el hilo del barbero
        
            // Crear hilos para los clientes
            for (int i = 0; i < numClientes; i++) {
                clientes[i] = new ClienteThread(i, barbero); // Pasar la referencia del barbero a cada cliente
                clientes[i].start();
                try {
                    Thread.sleep(new Random().nextInt(500)); // Simular llegadas aleatorias de clientes
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        
            // Esperar a que todos los clientes terminen
            for (int i = 0; i < numClientes; i++) {
                try {
                    clientes[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
