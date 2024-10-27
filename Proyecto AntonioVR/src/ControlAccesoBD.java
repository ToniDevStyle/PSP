import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ControlAccesoBD {

    //3 SERVIDORES
    private static final int NUM_SERVIDORES = 3;
    private static int servidoresTerminaronCache = 0;
    //SOLO 2 SERVIDORES PUEDEN ACCEDER A CACHE A LA VEZ
    private static Semaphore accesoCache = new Semaphore(2); 
    //BLOQUEA HASTA QUE TODOS TERMINEN EL CACHE PARA LUEGO PODER MODIFICAR LA BBDD
    private static Semaphore accesoModificacionBD = new Semaphore(0); 
    //SINCRONIZA OPERACIONES FINALES DE 1 EN 1
    private static Semaphore sincronizacionFinal = new Semaphore(1); 


      // Tarea de servidor que implementa Callable
      static class TareaServidor implements Callable<String> {
        private int id;

        public TareaServidor(int id) {
            this.id = id;
        }

        @Override
        public String call() throws Exception {
            // Simula las tres fases: acceso a caché, modificación de BD, operación final
            accesoCache(id);
            modificarBD(id);
            operacionFinal(id);
            return "Servidor " + id + " completó todas las operaciones.";
        }

    // Simulación de acceso a la caché (C0)
    private void accesoCache(int id) throws InterruptedException {
        // ACCESO CACHE
        accesoCache.acquire(); 
        System.out.println("Servidor " + id + " accede al caché.");
        // TRABAJO EN CACHE
        Thread.sleep((long) (Math.random() * 2000)); 
        System.out.println("Servidor " + id + " termina en el caché.");
        // LIBERA EL CACHE
        accesoCache.release(); 
        // CREAMOS UN METODO PARA COMPROBAR QUE TODOS LOS SERVIDORES HAN ACABADO EN LA CACHE
        servidorTerminoCache();
    }

    // METODO PARA COMPROBAR Y PERMITIR A LOS SERVIDORES ENTRAR EN LA BBDD
    private synchronized static void servidorTerminoCache() {
        servidoresTerminaronCache++;
        //COMPROBAMOS QUE TODOS LOS SERVIDORES HAYAN ACABADO EN CACHE
        if (servidoresTerminaronCache == NUM_SERVIDORES) {
            // DAMOS LAS 3 CLAVES AL SEMAFORO QUE CONTROLA EL ACCESO A LA BBDD
            accesoModificacionBD.release(NUM_SERVIDORES);
        }
    }

    
    private void modificarBD(int id) throws InterruptedException {
        // SERVIDORES VAN ACCEDIENDO A LA BBDD Y LA MODIFICAN
        accesoModificacionBD.acquire(); 
        System.out.println("Servidor " + id + " modifica la base de datos.");
        Thread.sleep((long) (Math.random() * 2000)); 
    }

    // SINCRONIZACION DE LAS MODIFICACIONES DE CADA SERVIDOR
    private void operacionFinal(int id) throws InterruptedException {
        // SINCRONIZACION DE 1 EN 1
        sincronizacionFinal.acquire(); 
        System.out.println("Servidor " + id + " realiza la sincronización final.");
        Thread.sleep((long) (Math.random() * 2000)); 
        // LIBERA EL SEMAFORO DE LA SINCRONIZACION PARA QUE ACCEDA EL SIGUIENTE SERVIDOR A LA SINCRONIZACION
        sincronizacionFinal.release(); 
    }
}

    public static void main(String[] args) {
        // 1. Creamos un ExecutorService con un pool de 3 hilos para ejecutar varias tareas
        // CREAMOS EL EXECUTOR COMO EN EL EJERCICIO DE EJEMPLO PARA EJECUTAR LA ACCION DE LOS 3 SERVIDORES
        ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVIDORES);
        // ALMACENAMOS LAS TAREAS DE LOS SERVIDORES EN UNA LISTA DE FUTURES
        List<Future<String>> futuros = new ArrayList<>();

        for (int i = 1; i <= NUM_SERVIDORES; i++) {
          
            //CREAMOS LA TAREA DEL SERVIDOR CON UN ID UNICO
            TareaServidor tarea = new TareaServidor(i);  
            // ENVIAMOS LA TAREA AL EXECUTOR SERVICE
            Future<String> future = executor.submit(tarea);  
            //GUARDAMOS EL FUTURE EN LAA LISTA
            futuros.add(future);  
        }

        // Ahora esperamos que todos los servidores terminen y obtenemos los resultados.
        for (Future<String> future : futuros) {
            try {
                System.out.println(future.get()); // Bloquea hasta que cada servidor termine.
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }
}
