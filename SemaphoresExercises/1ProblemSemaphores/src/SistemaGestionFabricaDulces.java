import java.util.Random; // Importamos la clase Random para generar números aleatorios.
import java.util.concurrent.Semaphore; // Importamos Semaphore para la gestión de concurrencia.

public class SistemaGestionFabricaDulces {

    //VARIABLES COMPARTIDAS SON LOS MISMOS SEMAFOROS EN ESTE CASO

    // Cinta transportadora con capacidad de 10 dulces (semaforo que limita el acceso a 10).
    // BandConveyor - 10 sweets limit
    private static Semaphore bandConveyor = new Semaphore(10, true);
    
    // Semáforo para rastrear la cantidad de dulces disponibles en la cinta (inicialmente 0).
    // Funciona a la inversa que el otro emaforo, en vez de ir consumiendo, va generando
    // Tracking how many sweets are available on the band conveyor
    private static Semaphore availableSweets = new Semaphore(0, true);
    
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
                // Thread starting, productor is making a sweet
                System.out.println("El productor " + id + " está produciendo un dulce");

                // El productor intenta adquirir un permiso del semáforo de la cinta transportadora.
                // Getting the bandConveyor sempahore key if its possible
                bandConveyor.acquire(); 
                
                // Simulamos un tiempo de producción aleatorio (hasta 5000 ms).
                // Random production time
                int sleepTime = new Random().nextInt(5000);
                Thread.sleep(sleepTime);

                // Mensaje indicando que el productor ha producido un dulce.
                // Productor has endend the sweet
                System.out.println("El productor " + id + " ha producido un dulce");
                
                // El productor libera un dulce, indicando que hay un dulce disponible para empaquetar.
                // Releasing the sweet, It is available to pack
                availableSweets.release(); 
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
                // No lo hacemos con tryAcquire porque el enunciado no menciona explicitamente que mientras el consumidor espera, deba hacer algo como por ejemplo decir que esta esperando
                // Simplemente el consumidor espera a que haya un dulce
                // El consumidor intenta adquirir un dulce de la cinta transportadora.
                // Consumer trying to get a sweet from the band conveyor
                availableSweets.acquire();
                
                //Consumer packing the sweet
                System.out.println("El consumidor " + id + " está empaquetando un dulce");

                
                int sleepTime = new Random().nextInt(5000);
                Thread.sleep(sleepTime);

                
                System.out.println("El consumidor " + id + " ha terminado de empaquetar un dulce");

                // El consumidor libera un permiso en la cinta, indicando que se ha retirado un dulce.
                // Consumer release a key from the bandConveyor
                bandConveyor.release(); 
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
        }
    }

    // Método principal que inicia la ejecución del programa.
    public static void main(String[] args) {
        int numThreads = 50; 
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
            threads[i].start(); 
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
    



// Cuando usas acquire(), el hilo que llama a este método se bloquea (se detiene) si no hay permisos disponibles en el semáforo. 
// El método tryAcquire() no bloquea el hilo. En su lugar, verifica inmediatamente si hay permisos disponibles. Si no hay, devuelve false, lo que significa que el hilo no se detendrá y continuará ejecutándose realizando otra accion