import java.util.Random;
import java.util.concurrent.Semaphore;

public class Barbero {

    // 1 Silla de barbero y 3 de espera (4 sillas total)
    private static Semaphore waitingChairs = new Semaphore(3); // 3 sillas de espera
    private static Semaphore barberChair = new Semaphore(1); // 1 silla de barbero
    private static Semaphore sleepingBarber = new Semaphore(0); // Para que el barbero duerma
    private static boolean busyBarber = false; // Variable de control para verificar si el barbero está ocupado

    //ME FALTA ESTO, (VARIABLE COMPARTIDA QUIZAS... CON CONTROL DE MUTEX)
    // El proceso de corte toma un tiempo aleatorio (simulado con Sleep). Durante este tiempo, el hilo correspondiente del barbero está ocupado.
    // "El barbero ha terminado de cortar el cabello del cliente X"
    static class BarberoThread extends Thread {

        @Override
        public void run() {
            //BUCLE INFINITO Y ESPERA DE SEMAFORO
            while (true) {
                try {
                    // Si hay un cliente, el barbero lo atiende
                    // Si no hay ningun cliente, el barbero duerme hasta que entre alguien
                    sleepingBarber.acquire();

                    barberChair.acquire(); // Bloquear la silla del barbero mientras corta
                    busyBarber = true; // El barbero está ocupado
                    // "El barbero está cortando el cabello del cliente X"
                    System.out.println("El barbero está cortando el cabello...");
                    Thread.sleep(new Random().nextInt(1000) + 1000); // Simular tiempo de corte de cabello
                    System.out.println("El barbero ha terminado de cortar el cabello.");
                    busyBarber = false; // El barbero ha terminado y ya no está ocupado

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //GARANTIZAMOS QUE LA SILLA (VARIABLE CRITICA/COMPARTIDA)SE LIBERE CORRECTAMENTE AUNQUE HAYA UNA EXCEPCION DURANTE LA EJECUCION DEL PROGRAMA.
                } finally {
                    barberChair.release(); // Liberar la silla cuando termina

                }
            }
        }
    }

    // Clase que extiende Thread para representar a los clientes
    static class ClienteThread extends Thread {

        private int id;

        public ClienteThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            // "El cliente X ha llegado a la barbería"
            System.out.println("El cliente " + id + " ha llegado a la barbería.");

            if (!busyBarber && barberChair.tryAcquire()) {
                // Si el barbero no está ocupado y la silla está libre, esta durmiendo, el cliente lo despierta
                System.out.println("El cliente " + id + " despierta al barbero.");
                sleepingBarber.release(); // Despertar al barbero
            } else if (waitingChairs.tryAcquire()) {
                //Si el barbero esta ocupado cuando el cliente llega, este se sienta en una de las sillas disponibles
                System.out.println("El cliente " + id + " se sienta a esperar.");
                try {
                    barberChair.acquire(); // Esperar a que la silla del barbero esté libre
                    waitingChairs.release(); // Liberar la silla de espera
                    System.out.println("El cliente " + id + " pasa a la silla del barbero.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // Si no hay sillas, se va "El cliente X se va porque no hay sillas"
                System.out.println("El cliente " + id + " se va porque no hay sillas disponibles.");
            }
        }

        public static void main(String[] args) {
            int numClientes = 100;
            ClienteThread[] clientes = new ClienteThread[numClientes];
            BarberoThread barbero = new BarberoThread();

            barbero.start();

            // Crear hilos para los clientes
            for (int i = 0; i < numClientes; i++) {
                clientes[i] = new ClienteThread(i);
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
}