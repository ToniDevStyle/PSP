
import java.util.Random;
import java.util.concurrent.Semaphore;

public class LectoresYEscritores {

    //VARIABLES DE CONTROL, DOS SEMAFOROS, UNO PARA CONTROLAR EL ACCESO A NUESTRAS VARIABLES CRITICAS, Y EL OTRO PARA CONTROLAR EL ACCESO A LA BASE DE DATOS
    //VARIABLES CRITICAS
    private static int readersCount = 0;       // Readers counter
    private static int writersCount = 0;       // Writers writting or waiting counter
    //SEMAFOROS
    private static Semaphore controlAccess = new Semaphore(1);  // Para controlar acceso a variables compartidas
    private static Semaphore dbAccess = new Semaphore(1);       // Para controlar acceso a la base de datos

    public static class Type0Thread extends Thread {

        private int id; 
    
        public Type0Thread(int id) {
            this.id = id;
        }
    
        @Override
        public void run() {
            try {
                System.out.println("El lector " + id + " ha llegado");
    
                // Protects variable acces for the readers
                controlAccess.acquire();  //Protege las variables de acceso a los lectores
    
                //Si hay escritores esperando, el lector debe esperar
                //If there are writers waiting, readers have to wait
                while (writersCount > 0) {
                    System.out.println("El lector " + id + " debe esperar.");
                    controlAccess.release();
                    // Espera mientras haya escritores esperando
                    while (writersCount > 0) {
                        Thread.sleep(100);
                    }
                    controlAccess.acquire();
                }
    
                // Incrementar el número de lectores
                readersCount++;
    
                // Si es el primer lector, bloquear el acceso de los escritores
                // If there is the first reader, blocking the acces for the writters. 
                if (readersCount == 1) {
                    dbAccess.acquire();  
                }
    
                controlAccess.release();  // Liberar el mutex para permitir que otros lectores accedan
    
                System.out.println("El lector " + id + " está accediendo a la base de datos");
                Thread.sleep((int) (Math.random() * 1000));  // Simula la lectura a la base de datos
    
                System.out.println("El lector " + id + " ha terminado de acceder a la base de datos");
    
                controlAccess.acquire();  // Adquirir el mutex para modificar el contador de lectores (variable critica)
    
                readersCount--;
    
                // Si es el último lector, liberar el semáforo para permitir que los escritores accedan
                if (readersCount == 0) {
                    dbAccess.release();
                }
    
                controlAccess.release();  // Liberar el mutex
    
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //ESCRITORES
    public static class Type1Thread extends Thread {

        private int id; 
    
        public Type1Thread(int id) {
            this.id = id;
        }
    
        @Override
        public void run() {
            try {
                //  PREGUNTAR SI HAY QUE COMPROBAR EN ESTE MOMENTO SI HAY MAS ESCRITORES QUE HAN LLEGADO O DESPUES, CUANDO REALMENTE VAYAN A MANIPULAR LA BBDD
                System.out.println("El escritor " + id + " ha llegado");
    
                controlAccess.acquire();  // Protege el acceso a las variables de control (readersCount)
    
                // Si hay lectores activos, el escritor debe esperar
                while (readersCount > 0) {
                    System.out.println("El escritor " + id + " debe esperar, hay lectores activos.");
                    controlAccess.release();
                    while (readersCount > 0) {
                        Thread.sleep(100);  // Espera activa mientras haya lectores
                    }
                    controlAccess.acquire();
                }
    
                // Incrementar el número de escritores esperando
                writersCount++;
                controlAccess.release();  // Liberar el mutex
    
                // Adquirir el semáforo de la base de datos (solo puede haber un escritor a la vez)
                dbAccess.acquire();  // El escritor adquiere el semáforo de la base de datos
    
                System.out.println("El escritor " + id + " está accediendo a la base de datos");
                Thread.sleep((int) (Math.random() * 1000));  // Simula el tiempo de escritura
    
                System.out.println("El escritor " + id + " ha terminado de acceder a la base de datos");
    
                // Liberar el semáforo de la base de datos
                dbAccess.release();
    
                controlAccess.acquire();  // Adquirir mutex para modificar writersCount
                writersCount--;  // Decrementar el número de escritores esperando
                controlAccess.release();  // Liberar mutex
    
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        int numThreads = 20;
        Thread[] threads = new Thread[numThreads];
        Random random = new Random();

        for (int i = 0; i < numThreads; i++) {
            // Generar un hilo aleatoriamente como lector o escritor
            if (random.nextBoolean()) {
                threads[i] = new Type0Thread(i);
            } else {
                threads[i] = new Type1Thread(i);
            }
            threads[i].start();
        }

        // Esperar a que todos los hilos terminen
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
