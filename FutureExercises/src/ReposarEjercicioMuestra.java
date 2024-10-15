import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ReposarEjercicioMuestra{


   // Definimos una tarea que implementa Callable y devuelve un valor entero
   static class TareaLarga implements Callable<Integer> {
       private int id;


       public TareaLarga(int id) {
           this.id = id;
       }


       @Override
       public Integer call() throws Exception {
           System.out.println("Tarea " + id + " está en ejecución...");
           Thread.sleep(2000);  // Simulamos una tarea que tarda 2 segundos en completarse
           return id * 10;  // Devolvemos un resultado que depende del ID de la tarea
       }
   }


   public static void main(String[] args) {
       // 1. Creamos un ExecutorService con un pool de 3 hilos para ejecutar varias tareas
       ExecutorService executor = Executors.newFixedThreadPool(3);


       // 2. Creamos una lista para almacenar las tareas
       List<Future<Integer>> futures = new ArrayList<>();


       // 3. Enviamos tres tareas al executor para que se ejecuten en paralelo
       for (int i = 1; i <= 3; i++) {
           TareaLarga tarea = new TareaLarga(i);  // Creamos una nueva tarea con un ID único
           Future<Integer> future = executor.submit(tarea);  // Enviamos la tarea al ExecutorService
           futures.add(future);  // Guardamos el Future en la lista
       }


       // 4. Ahora obtenemos los resultados de las tareas
       for (Future<Integer> future : futures) {
           try {
               // Esto bloqueará hasta que cada tarea haya terminado
               Integer resultado = future.get();
               System.out.println("Resultado de la tarea: " + resultado);
           } catch (InterruptedException | ExecutionException e) {
               e.printStackTrace();
           }
       }


       // 5. Apagamos el ExecutorService para liberar recursos
       executor.shutdown();
   }
}
